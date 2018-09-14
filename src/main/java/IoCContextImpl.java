import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class IoCContextImpl implements IoCContext {

    Set<Class<?>> containerSet = new HashSet<>();

    @Override
    public void registerBean(Class<?> beanClazz) {
        containerSet.add(beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        if (resolveClazz == null || !containerSet.contains(resolveClazz))
            throw new IllegalArgumentException("resolveClazz is mandatory");

        try {
            return resolveClazz.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(String.format("%s is abstract", resolveClazz.getName()));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(String.format("%s has no default constructor", resolveClazz.getName()));
        }
    }
}
