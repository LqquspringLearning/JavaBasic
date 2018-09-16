package testClass;

public class MyException extends Exception {

    public MyException() {
        super("this is My exception !");
    }

    public MyException(String message) {
        super(message);
    }
}
