package testClass;

import java.util.Date;

public class MyDependency implements AutoCloseable {


    private final Date time;

    private Date closeTime;

    public MyDependency() {
        this.time = new Date();

    }

    public long getTime() {
        return time.getTime();
    }

    public long getCloseTime() {
        return closeTime.getTime();
    }

    @Override
    public void close() throws Exception {
        this.closeTime = new Date();
    }
}
