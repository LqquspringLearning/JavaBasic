package testClass;

import IocContainer.CreateOnTheFly;

public class MyBeanWithAnnotationParentClass {

    @CreateOnTheFly
    protected MyDependencyAbstract myDependencyAbstract;

    public MyDependencyAbstract getMyDependencyAbstract() {
        return myDependencyAbstract;
    }



}
