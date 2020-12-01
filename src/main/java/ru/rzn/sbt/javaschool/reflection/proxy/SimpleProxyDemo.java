package ru.rzn.sbt.javaschool.reflection.proxy;

import ru.rzn.sbt.javaschool.reflection.proxy.data.RealClass;
import ru.rzn.sbt.javaschool.reflection.proxy.data.TargetInterface;

/**
 * Прокси-объект, который перехватывает действия. Мы его создали сами.
 */
class SimpleProxy implements TargetInterface {
    private TargetInterface proxied;

    public SimpleProxy(TargetInterface proxied) {
        this.proxied = proxied;
    }

    public void myTestMethod1() {
        System.out.println("SimpleProxy, выполнение метода - myTestMethod1");
        proxied.myTestMethod1();
    }

    public void myTestMethod2(String arg) {
        System.out.println("SimpleProxy, выполнение метода - myTestMethod2 " + arg);
        proxied.myTestMethod2(arg);
    }

    public void getPicture(String sessionId, String userName) {

    }
}

class SimpleProxyDemo {
    public static void consumer(TargetInterface iface) {
        iface.myTestMethod1();
        iface.myTestMethod2("banana");
    }

    public static void main(String[] args) {
        consumer(new RealClass());
        consumer(new SimpleProxy(new RealClass()));
    }
}
