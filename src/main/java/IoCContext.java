public interface IoCContext {
    void registerBean(Class<?> beanClazz);
    <T> T getBean(Class<T> resolveClazz);
}
