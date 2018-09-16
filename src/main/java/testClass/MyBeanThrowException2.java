package testClass;

public class MyBeanThrowException2 implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new MyException("MyBeanThrowException2");
    }
}
