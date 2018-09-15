public class MyBean {
    public String message;
    @CreateOnTheFly
    private MyDenpendency denpendency;

    public MyDenpendency getDenpendency() {
        return denpendency;
    }

    public MyBean(){
        message ="MyBeanMsg";
    }
}
