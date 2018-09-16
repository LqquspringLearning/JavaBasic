package testClass;

import java.util.Date;

public class MyDependency {


    private final Date time;

    public MyDependency() {
        this.time = new Date();
    }

    public long getTime() {
        return time.getTime();
    }
}
