package testClass;

public class MyBeanInterfaceImplAndSubClass extends MyBean implements MyBeanInterface {

    public MyBeanInterfaceImplAndSubClass() {
        message = "testClass.MyBeanInterfaceImplAndSubClass";
    }

    @Override
    public String Hello() {
        return "Hello";
    }
}
