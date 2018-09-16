package testClass;

import java.util.Date;

public abstract class MyDependencyAbstract {

    private Date time;

    public MyDependencyAbstract() {
        time = new Date();
    }


    public long getTime() {
        return time.getTime();
    }
}
