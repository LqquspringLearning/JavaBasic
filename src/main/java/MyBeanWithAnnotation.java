public class MyBeanWithAnnotation {
    @CreateOnTheFly
    private MyDependency dependency;

    public MyDependency getDependency() {
        return dependency;
    }
}
