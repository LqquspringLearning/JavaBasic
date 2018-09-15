public class MyBeanWithInterface {
    public MyDependency getDependency() {
        return dependency;
    }

    public MyDependencyInterface getMyDependencyInterface() {
        return myDependencyInterface;
    }

    public MyDependencyAbstract getMyDependencyAbstract() {
        return myDependencyAbstract;
    }

    @CreateOnTheFly
    private MyDependency dependency;
    @CreateOnTheFly
    private MyDependencyInterface myDependencyInterface;
    @CreateOnTheFly
    private MyDependencyAbstract myDependencyAbstract;
}
