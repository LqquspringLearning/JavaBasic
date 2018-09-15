import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

public class IoCContextImpl implements IoCContext {

    Map<Map<Class<?>, Class<?>>, Boolean> containerMap = new HashMap<>();

    @Override
    public void registerBean(Class<?> beanClazz) {
        if (beanClazz == null)
            throw new IllegalArgumentException("resolveClazz is mandatory");
        if (containerMap.containsKey(beanClazz)) {
            if (containerMap.get(beanClazz)) {
                throw new IllegalStateException();
            }
        }
        Map<Class<?>, Class<?>> subMap = new HashMap<>();
        subMap.put(beanClazz, beanClazz);
        containerMap.put(subMap, false);
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) {
        if (!(beanClazz.getSuperclass().equals(resolveClazz))) {
            if (!(Arrays.stream(beanClazz.getInterfaces()).filter(i -> i.equals(resolveClazz)).count() > 0))
                throw new IllegalArgumentException();
        }
        Map<Class<?>, Class<?>> subMap = new HashMap<>();
        subMap.put(resolveClazz, beanClazz);
        containerMap.put(subMap, false);
    }


    @Override
    public <T> T getBean(Class<T> resolveClazz) {

        if (resolveClazz == null)
            throw new IllegalArgumentException("resolveClazz is mandatory");

        if (!containerMapContainKey(resolveClazz))
            throw new IllegalStateException("resolveClazz is mandatory");

        try {
            T resultBean = generateInstance(resolveClazz);
            Field[] resultBeanFields = resultBean.getClass().getDeclaredFields();
            Field[] containsCreateOnTheFlyAnnotationFields = Arrays.stream(resultBeanFields).
                    filter(getContainsCreateOnTheFlyAnnotationFields()).toArray(Field[]::new);
            checkFieldsIsRegistered(containsCreateOnTheFlyAnnotationFields);
            Arrays.stream(containsCreateOnTheFlyAnnotationFields).forEach(field -> {

                Object filedInstance = this.getBean(field.getType());

                Field resultBeanField;
                try {
                    resultBeanField = resultBean.getClass().getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException("no such field");
                }
                resultBeanField.setAccessible(true);
                try {
                    resultBeanField.set(resultBean, filedInstance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                resultBeanField.setAccessible(false);
            });
            return resultBean;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException();
        } catch (InstantiationException e) {
            return handleCreateInstanceException(resolveClazz);
        }

    }

    private <T> T generateInstance(Class<T> resolveClazz) throws InstantiationException, IllegalAccessException {
        List<Class<T>> instanceValue = new ArrayList<>();
        instanceValue.add(resolveClazz);
        containerMap.forEach((key, value) -> {
            if (key.containsKey(resolveClazz)) {
                instanceValue.set(0, (Class<T>) key.get(resolveClazz));
            }
        });
        return instanceValue.get(0).newInstance();

    }

    private <T> T handleCreateInstanceException(Class<T> resolveClazz) {
        ModiferIsAbstract(resolveClazz);
        return noConstructor(resolveClazz);
    }

    private <T> T noConstructor(Class<T> resolveClazz) {
        throw new IllegalStateException(String.format("%s has no default constructor", resolveClazz.getName()));
    }

    private <T> void ModiferIsAbstract(Class<T> resolveClazz) {
        if (Modifier.isAbstract(resolveClazz.getModifiers())) {
            throw new IllegalArgumentException(String.format("%s is abstract", resolveClazz.getName()));
        }
    }

    private Predicate<Field> getContainsCreateOnTheFlyAnnotationFields() {
        return field ->
                Arrays.stream(field.getAnnotations())
                        .filter(annotation ->
                                annotation.annotationType().equals(CreateOnTheFly.class))
                        .count() > 0;
    }

    private void checkFieldsIsRegistered(Field[] containsMyDependencyAnnotationFields) {
        Arrays.stream(containsMyDependencyAnnotationFields).forEach(field -> {
            if (!containerMapContainKey(field.getType())) {
                throw new IllegalStateException();
            }
        });
    }

    private <T> boolean containerMapContainKey(Class<T> resolveClazz) {
        boolean[] contains = {false};
        containerMap.forEach((key, value) -> {
            if (key.containsValue(resolveClazz) || key.containsKey(resolveClazz)) {
                contains[0] = true;
            }
        });
        return contains[0];
    }
}
