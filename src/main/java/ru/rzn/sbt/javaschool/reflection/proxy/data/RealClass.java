package ru.rzn.sbt.javaschool.reflection.proxy.data;

/**
 * Настоящий класс, который выполняет действия.
 */
public class RealClass implements TargetInterface {
    public void myTestMethod1() {
        System.out.println("RealObject, выполнение метода myTestMethod1");
    }

    public void myTestMethod2(String arg) {
        System.out.println("RealObject, выполнение метода myTestMethod2 " + arg);
    }

    public void getPicture(String sessionId, String userName) {
        System.out.println("Получение картинки пользователя: " + userName + ", с sessionID: " + sessionId);
    }
}
