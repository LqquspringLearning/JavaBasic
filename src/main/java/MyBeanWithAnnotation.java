public class MyBeanWithAnnotation {
    @CreateOnTheFly
    private MyDependency dependency;

    @CreateOnTheFly
    private MyDependency dependency2;

    public String getMessage() {
        return message;
    }

    private String message;

    public MyDependency getDependency() {
        return dependency;
    }

    public MyDependency getDependency2() {
        return dependency2;
    }
}
