import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

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
        } catch (IllegalArgumentException e) {
            assertSame(IllegalArgumentException.class, e.getClass());
            assertEquals("MyBeanAnother is abstract", e.getMessage());
        }
    }

    @Test
    void should_nothing_happen_when_beanClazz_already_registred() {
        IoCContext context = new IoCContextImpl();
        boolean happenSomething = false;
        try {
            context.registerBean(MyBean.class);
            context.registerBean(MyBean.class);
        } catch (Exception e) {
            happenSomething = true;
        }
        assertFalse(happenSomething);
    }

    @Test
    void should_throw_a_exception_when_start_call_getBean_mathod() {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBean.class);
        Stream<Integer> stream = Stream.iterate(1, i -> i).limit(10000);
        try {
            stream.parallel().map(item -> {
                context.getBean(MyBean.class);
                return item;
            }).count();
        } catch (IllegalStateException e) {
            assertSame(IllegalStateException.class, e.getClass());
        }

    }
}