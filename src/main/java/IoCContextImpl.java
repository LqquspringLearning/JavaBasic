import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

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

        if (!containerMapcontainKey(resolveClazz))
            throw new IllegalStateException("resolveClazz is mandatory");

        try {
            return resolveClazz.newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException();
        } catch (InstantiationException e) {
            if (Modifier.isAbstract(resolveClazz.getModifiers())) {
                throw new IllegalArgumentException(String.format("%s is abstract", resolveClazz.getName()));
            }
            throw new IllegalStateException(String.format("%s has no default constructor", resolveClazz.getName()));
        }

    }

    private <T> boolean containerMapcontainKey(Class<T> resolveClazz) {
        boolean[] contains = {false};
        containerMap.forEach((key, value) -> {
            if (key.containsValue(resolveClazz)||key.containsKey(resolveClazz)) {
                contains[0] = true;
            }
        });
        return contains[0];
    }
}
