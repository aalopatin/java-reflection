package ru.rzn.sbt.javaschool.reflection.proxy.data;

/**
 * Общий интерфейс для интересующих нас объектов.
 */
public interface TargetInterface {
    void myTestMethod1();

    void myTestMethod2(String arg);

    void getPicture(String sessionId, String userName);
}
