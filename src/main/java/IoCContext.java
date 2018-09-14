public interface IoCContext {
    void registerBean(Class<?> beanClazz);
    <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz);
    <T> T getBean(Class<T> resolveClazz);
}
