package ru.rzn.sbt.javaschool.reflection.base.modifiers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassModifiersAnalyzer {
    private static SomeClassWithPrivates someClassWithPrivates;

    public static void changePrivateIntField() {
        Field field = null;

        try {
            field = someClassWithPrivates.getClass().getDeclaredField("myIntegerValue");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        field.setAccessible(true);

        try {
            field.setInt(someClassWithPrivates, 47);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(someClassWithPrivates);
    }

    public static void changePrivateStrField(String name) {
        Field field = null;

        try {
            field = someClassWithPrivates.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        field.setAccessible(true);

        try {
            field.set(someClassWithPrivates, "MODIFY - " + field.get(someClassWithPrivates));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(someClassWithPrivates);
    }

    public static void getModifiersFromField(String name) throws Exception {
        Field field = someClassWithPrivates.getClass().getDeclaredField(name);
        int modifiers = field.getModifiers();
        System.out.println("isAbstract - " + Modifier.isAbstract(modifiers));
        System.out.println("isFinal - " + Modifier.isFinal(modifiers));
        System.out.println("isInterface - " + Modifier.isInterface(modifiers));
        System.out.println("isNative - " + Modifier.isNative(modifiers));
        System.out.println("isPrivate - " + Modifier.isPrivate(modifiers));
        System.out.println("isProtected - " + Modifier.isProtected(modifiers));
        System.out.println("isPublic - " + Modifier.isPublic(modifiers));
        System.out.println("isStatic - " + Modifier.isStatic(modifiers));
        System.out.println("isStrict - " + Modifier.isStrict(modifiers));
        System.out.println("isSynchronized - " + Modifier.isSynchronized(modifiers));
        System.out.println("isTransient - " + Modifier.isTransient(modifiers));
        System.out.println("isVolatile - " + Modifier.isVolatile(modifiers));
    }

    public static void main(String[] args) throws Exception {
        someClassWithPrivates = new SomeClassWithPrivates();

        System.out.println("Первоначальное значение: ");
        System.out.println(someClassWithPrivates);

        System.out.println("Пробуем изменить int значение: ");
        changePrivateIntField();

        System.out.println("Пробуем изменить simpleString значение: ");
        changePrivateStrField("myString");

        System.out.println("Пробуем изменить finalStringValue значение: ");
        changePrivateStrField("myFinalString");

        System.out.println("Список модификаторов поля finalStringValue:");
        getModifiersFromField("myFinalString");
    }

}
