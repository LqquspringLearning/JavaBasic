public class MyBeanWithAnnotationParentClass {
    @CreateOnTheFly
    protected MyDependencyAbstract myDependencyAbstract;

    public MyDependencyAbstract getMyDependencyAbstract() {
        return myDependencyAbstract;
    }

}
