import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IoCContextImplTest {
    IoCContext context;

    @BeforeEach
    void setUp() {
        context = new IoCContextImpl();
    }

    @Test
    void should_create_a_instance() {

        context.registerBean(MyBean.class);
        MyBean myBeanInstance = context.getBean(MyBean.class);
        assertEquals(MyBean.class, myBeanInstance.getClass());
    }

    @Test
    void should_throw_exception_when_class_null() {
        context.registerBean(MyBean.class);
        try {
            context.getBean(null);
        } catch(IllegalArgumentException e){
            assertSame(IllegalArgumentException.class,e.getClass());
           assertEquals("resolveClazz is mandatory",e.getMessage());
        }

    }

    @Test
    void should_throw_when_class_not_register() {
        try {
            context.getBean(MyBean.class);
        } catch(IllegalArgumentException e){
            assertSame(IllegalArgumentException.class,e.getClass());
            assertEquals("resolveClazz is mandatory",e.getMessage());
        }

    }

    @Test
    void should_throw_excetion_when_class_is_abstract() {
        context.registerBean(MyBeanAbstractClass.class);
      try {
          context.getBean(MyBeanAbstractClass.class);
      } catch(IllegalArgumentException e){
          assertSame(IllegalArgumentException.class, e.getClass());
          assertEquals("MyBeanAbstractClass is abstract",e.getMessage());
      }
    }

    @Test
    void should_throw_exception_when_calss_need_param_when_constract() {
     context.registerBean(MyBeanAnother.class);
     try {
         context.getBean(MyBeanAnother.class);
     } catch(IllegalArgumentException e){
         assertSame(IllegalArgumentException.class, e.getClass());
         assertEquals("MyBeanAnother is abstract",e.getMessage());
     }
    }

    @Test
    void should_nothing_happen_when_beanClazz_already_registred() {
        boolean happenSomething = false;
        try {
            context.registerBean(MyBean.class);
            context.registerBean(MyBean.class);
        } catch( Exception e){
            happenSomething = true;
        }
        assertFalse(happenSomething);
    }
}