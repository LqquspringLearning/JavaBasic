public class MyBeanInterfaceImplAndSubClass extends MyBean implements MyBeanInterface {

    public MyBeanInterfaceImplAndSubClass() {
        message = "MyBeanInterfaceImplAndSubClass";
    }

    @Override
    public String Hello() {
        return "Hello";
    }
}
