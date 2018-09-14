import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class IoCContextImpl implements IoCContext {

    Set<Class<?>> containerSet = new HashSet<>();
    Object locker = new Object();
    public boolean available = true;

    @Override
    public void registerBean(Class<?> beanClazz) {
        containerSet.add(beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {

        if (!available) throw new IllegalStateException();

        synchronized (locker) {
            available = false;
            if (resolveClazz == null)
                throw new IllegalArgumentException("resolveClazz is mandatory");
            if (!containerSet.contains(resolveClazz))
                throw new IllegalStateException("resolveClazz is mandatory");
            try {
                return resolveClazz.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(String.format("%s is abstract", resolveClazz.getName()));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(String.format("%s has no default constructor", resolveClazz.getName()));
            }
            finally {
                available = true;
            }
        }

    }
}
