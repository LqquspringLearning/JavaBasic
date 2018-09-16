package testClass;

import java.util.Date;

public class MyBean implements AutoCloseable {
    public String message;
    private Date closeTime;

    public MyBean() {
        message = "MyBeanMsg";
    }

    public long getCloseTime() {
        return closeTime.getTime();
    }

    @Override
    public void close() throws Exception {
        closeTime = new Date();
    }
}
