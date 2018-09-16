package testClass;

import java.util.Date;

public abstract class MyDependencyAbstract {

    protected Date time;
    protected Date closeTime;

    public MyDependencyAbstract() {
        time = new Date();
    }


    public long getTime() {
        return time.getTime();
    }
}
