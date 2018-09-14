import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IoCContextImplTest {


    @Test
    void should_create_a_instance() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        MyBean myBeanInstance = context.getBean(MyBean.class);
        assertEquals(MyBean.class, myBeanInstance.getClass());
    }


    @Test
    void should_throw_when_class_not_register() {
        IoCContext context = new IoCContextImpl();
        try {
            context.getBean(MyBean.class);
        } catch (IllegalStateException e) {
            assertSame(IllegalStateException.class, e.getClass());
        }

    }


    @Test
    void should_throw_exception_when_class_is_abstract() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanAbstractClass.class);
        try {
            context.getBean(MyBeanAbstractClass.class);
        } catch (IllegalArgumentException e) {
            assertSame(IllegalArgumentException.class, e.getClass());
            assertEquals("MyBeanAbstractClass is abstract", e.getMessage());
        }
    }

    @Test
    void should_throw_exception_when_register_type_is_interface() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(IoCContext.class);
        try {
            context.getBean(IoCContext.class);
        } catch (IllegalArgumentException e) {
            assertSame(IllegalArgumentException.class, e.getClass());
            assertEquals("IoCContext is abstract", e.getMessage());
        }
    }

    @Test
    void should_throw_exception_when_class_need_param_when_constract() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanAnother.class);
        try {
            context.getBean(MyBeanAnother.class);
        } catch (IllegalStateException e) {
            assertSame(IllegalStateException.class, e.getClass());
            assertEquals("MyBeanAnother has no default constructor", e.getMessage());
        }
    }

    @Test
    void should_nothing_happen_when_beanClazz_already_registred() {
        IoCContext context = new IoCContextImpl();
        boolean hasException = false;
        try {
            context.registerBean(MyBean.class);
            context.registerBean(MyBean.class);
        } catch (Exception e) {
            hasException = true;
        }
        assertFalse(hasException);
    }

    @Test
    void should_continued_throw_exception_when_contructor_thorw_exception() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanContructorWithException.class);
        try {
            context.getBean(MyBeanContructorWithException.class);
        } catch (Exception e) {
            assertEquals("this is My exception !", e.getMessage());
        }
    }

    @Test
    void should_get_instance_when_multiple_bean() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        context.registerBean(MyBeanAnother.class);
        MyBean myBeanInstance = context.getBean(MyBean.class);
        assertEquals(MyBean.class, myBeanInstance.getClass());
    }

    @Test
    void should_get_multiple_time_when_already_registered() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        MyBean instance1 = context.getBean(MyBean.class);
        MyBean instance2 = context.getBean(MyBean.class);
        assertEquals(MyBean.class, instance1.getClass());
        assertEquals(MyBean.class, instance2.getClass());
        assertNotSame(instance1,instance2);
    }

    @Test
    void should_throw_a_exception_when_start_call_getBean_method() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        context.getBean(MyBean.class);
        try {
            context.registerBean(MyBean.class);
        } catch (IllegalStateException e) {
            assertSame(IllegalStateException.class, e.getClass());
        }

    }

    @Test
    void should_throw_exception_when_class_null() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        try {
            context.getBean(null);
        } catch (IllegalArgumentException e) {
            assertSame(IllegalArgumentException.class, e.getClass());
            assertEquals("resolveClazz is mandatory", e.getMessage());
        }

    }

    @Test
    void should_throw_exception_when_register_null() {
        IoCContext context = new IoCContextImpl();
        try {
            context.registerBean(null);
        } catch(IllegalArgumentException e){
            assertEquals("resolveClazz is mandatory", e.getMessage());
        }


    }

    @Test
    void should_throw_exception_when_register_one_and_get_another() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanAnother.class);
        try {
            context.getBean(MyBean.class);
        } catch (IllegalStateException e) {
            assertSame(IllegalStateException.class, e.getClass());
        }
    }

}