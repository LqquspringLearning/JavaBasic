package testClass;

public class MyBeanThrowException implements  AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new MyException("MyBeanThrowException");
    }
}
