import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class IoCContextImpl implements IoCContext {

    Map<Class<?>, Boolean> containerMap = new HashMap<>();

    @Override
    public void registerBean(Class<?> beanClazz) {
        if (beanClazz == null)
            throw new IllegalArgumentException("resolveClazz is mandatory");
        if(containerMap.containsKey(beanClazz)){
            if(containerMap.get(beanClazz)){
                throw new IllegalStateException();
            }
        }
        containerMap.put(beanClazz, false);
    }




    @Override
    public <T> T getBean(Class<T> resolveClazz) {

        if (resolveClazz == null)
            throw new IllegalArgumentException("resolveClazz is mandatory");
        if (!containerMap.containsKey(resolveClazz))
            throw new IllegalStateException("resolveClazz is mandatory");

        try {
            return resolveClazz.newInstance();
        }
        catch (IllegalAccessException e) {
           throw new IllegalStateException();
        }
        catch (InstantiationException e) {
            if(Modifier.isAbstract(resolveClazz.getModifiers())){
                throw new IllegalArgumentException(String.format("%s is abstract", resolveClazz.getName()));
            }
            throw new IllegalStateException(String.format("%s has no default constructor", resolveClazz.getName()));
        }

    }
}
