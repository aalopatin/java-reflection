package ru.rzn.sbt.javaschool.reflection.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Объект динамического прокси, принимает реальный объект в качестве параметра.
 */
class MethodSelector implements InvocationHandler {
    private Object proxied;

    public MethodSelector(Object proxied) {
        this.proxied = proxied;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("interesting")) {
            System.out.println("Мы обнаружили интересующий нас метод, можем что-то сделать перед его началом.");
        }
        return method.invoke(proxied, args);
    }
}

/**
 * Некоторый набор методов.
 */
interface SomeMethods {
    void testMethod1();

    void testMethod2();

    void interesting(String arg);

    void testMethod3();
}

/**
 * Реализация методов.
 */
class Implementation implements SomeMethods {
    public void testMethod1() {
        System.out.println("testMethod1");
    }

    public void testMethod2() {
        System.out.println("testMethod2");
    }

    public void interesting(String arg) {
        System.out.println("interesting " + arg);
    }

    public void testMethod3() {
        System.out.println("testMethod3");
    }
}

class SelectingMethods {
    public static void main(String[] args) {
        SomeMethods proxy = (SomeMethods) Proxy.newProxyInstance(
                SomeMethods.class.getClassLoader(),
                new Class[]{SomeMethods.class},
                new MethodSelector(new Implementation()));
        proxy.testMethod1();
        proxy.testMethod2();
        proxy.interesting("banana");
        proxy.testMethod3();
    }
}
