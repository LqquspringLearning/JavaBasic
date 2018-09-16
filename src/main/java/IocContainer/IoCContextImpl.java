package IocContainer;

import Tools.ReflectorHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class IoCContextImpl implements IoCContext {

    Map<Map<Class<?>, Class<?>>, Boolean> containerMap = new HashMap<>();
    Map<Class<?>, Object> cache = new LinkedHashMap<>();
    private Exception closeException;

    @Override
    public void registerBean(Class<?> beanClazz) {
        paramCheck(beanClazz);
        putBeanToContainer(beanClazz, beanClazz);
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) {
        paramCheck(resolveClazz, beanClazz);
        putBeanToContainer(resolveClazz, beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        return getBean(resolveClazz, true);
    }

    private <T> T getBean(Class<T> resolveClazz, boolean isTransient) {
        paramNullCheck(resolveClazz);
        if (!containerMapContainKey(resolveClazz))
            throw new IllegalStateException("resolveClazz is mandatory");
        try {
            T resultBean = null;
            if (!isTransient) {
                resultBean = tryGetFromCache(resolveClazz);
            }
            if (resultBean == null)
                resultBean = generateInstance(resolveClazz);
            return resultBean;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException();
        } catch (InstantiationException e) {
            return handleCreateInstanceException(resolveClazz);
        }
    }

    private <T> T tryGetFromCache(Class<T> resolveClazz) {
        return (T) cache.get(resolveClazz);
    }

    @Override
    public void close() throws Exception {
        List<Class<?>> keySetList = reverseKeySetList();
        keySetList.forEach(closeInstanceConsumer());
        if (closeException != null)
            throw closeException;
    }

    private Consumer<Class<?>> closeInstanceConsumer() {
        return key -> {
            closeInstance(key);
        };
    }

    private void closeInstance(Object key) {
        Object instance = cache.get(key);
        if (IsContainThisInterface(AutoCloseable.class, instance.getClass())) {
            AutoCloseable autoCloseable = (AutoCloseable) instance;

            try {
                autoCloseable.close();
            } catch (Exception e) {
                setException(e);
            }
        }
    }

    private void setException(Exception e) {
        if (closeException != null) return;
        closeException = e;
    }

    private List<Class<?>> reverseKeySetList() {
        List<Class<?>> keySetList = new ArrayList<>(cache.keySet());
        Collections.reverse(keySetList);
        return keySetList;
    }


    private <T> void paramCheck(Class<? super T> resolveClazz, Class<T> beanClazz) {
        if (!isInheritRelation(resolveClazz, beanClazz)) {
            if (!IsContainThisInterface(resolveClazz, beanClazz))
                throw new IllegalArgumentException();
        }
    }

    private void paramCheck(Class<?> beanClazz) {
        paramNullCheck(beanClazz);
        if (containerMap.containsKey(beanClazz)) {
            if (alreadyUsed(beanClazz)) {
                throw new IllegalStateException();
            }
        }
    }

    private Boolean alreadyUsed(Class<?> beanClazz) {
        return containerMap.get(beanClazz);
    }

    private void paramNullCheck(Class<?> beanClazz) {
        if (beanClazz == null)
            throw new IllegalArgumentException("resolveClazz is mandatory");
    }

    private boolean IsContainThisInterface(Class<?> interfaceType, Class<?> beanType) {
        return Arrays.stream(beanType.getInterfaces()).filter(i -> i.equals(interfaceType)).count() > 0;
    }

    private <T> boolean isInheritRelation(Class<? super T> resolveClazz, Class<T> beanClazz) {
        return beanClazz.getSuperclass().equals(resolveClazz);
    }

    private void putBeanToContainer(Class<?> resolveClazz, Class<?> beanClazz) {
        Map<Class<?>, Class<?>> subMap = new HashMap<>();
        subMap.put(resolveClazz, beanClazz);
        containerMap.put(subMap, false);
    }

    private <T> Consumer<Field> generateFieldInstance(T resultBean, Field[] resultBeanFields) {
        return field -> {
            Object filedInstance = this.getBean(field.getType());
            Field resultBeanField = Arrays.stream(resultBeanFields).filter(f -> f.getName().equals(field.getName())).findFirst().get();
            resultBeanField.setAccessible(true);
            try {
                resultBeanField.set(resultBean, filedInstance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            resultBeanField.setAccessible(false);
        };
    }

    private <T> T generateInstance(Class<T> resolveClazz) throws InstantiationException, IllegalAccessException {
        T resultBean = getInstanceType(resolveClazz).newInstance();
        Field[] resultBeanFields = ReflectorHelper.getAllFields(resolveClazz);
        Field[] containsCreateOnTheFlyAnnotationFields = Arrays.stream(resultBeanFields).
                filter(getContainsCreateOnTheFlyAnnotationFields()).toArray(Field[]::new);
        checkFieldsIsRegistered(containsCreateOnTheFlyAnnotationFields);
        Arrays.stream(containsCreateOnTheFlyAnnotationFields).forEach(generateFieldInstance(resultBean, resultBeanFields));
        addToCache(resolveClazz, resultBean);
        return resultBean;
    }

    private <T> void addToCache(Class<T> resolveClazz, T resultBean) {
        cache.put(resolveClazz, resultBean);
    }

    private <T> boolean isCached(Class<T> resolveClazz) {
        return cache.containsKey(resolveClazz);
    }

    private <T> Class<T> getInstanceType(Class<T> resolveClazz) {
        List<Class<T>> instanceType = new ArrayList<>();
        instanceType.add(resolveClazz);
        containerMap.forEach((key, value) -> {
            if (key.containsKey(resolveClazz)) {
                instanceType.set(0, (Class<T>) key.get(resolveClazz));
            }
        });
        return instanceType.get(0);
    }

    private static <T> T handleCreateInstanceException(Class<T> resolveClazz) {
        ModifierIsAbstract(resolveClazz);
        return noConstructor(resolveClazz);
    }

    private static <T> T noConstructor(Class<T> resolveClazz) {
        throw new IllegalStateException(String.format("%s has no default constructor", resolveClazz.getName()));
    }

    private static <T> void ModifierIsAbstract(Class<T> resolveClazz) {
        if (Modifier.isAbstract(resolveClazz.getModifiers())) {
            throw new IllegalArgumentException(String.format("%s is abstract", resolveClazz.getName()));
        }
    }

    private static Predicate<Field> getContainsCreateOnTheFlyAnnotationFields() {
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
