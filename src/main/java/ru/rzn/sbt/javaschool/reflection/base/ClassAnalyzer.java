package ru.rzn.sbt.javaschool.reflection.base;

import java.lang.reflect.*;

/**
 * Демонстрация основных возможностей рефлексии.
 */
public class ClassAnalyzer {
    public static final String TEST_STRING = "testString";

    public static void main(String[] args) {
        // Шаг 1
        // Рассмотрим стандартный спобос создания объекта
        TheCat theCat = new TheCat();
        System.out.println("Голос по умолчанию: " + theCat.getVoice());

        // на текущий момент у нас некий экземпляр класса, и из него мы можем понять какой класс перед нами
        Class classFromObject = theCat.getClass();
        Class classFromClass = TheCat.class;

        // получение имени класса
        String className = classFromObject.getName();
        String classNameFromKey = classFromClass.getName();
        String classNameString = TEST_STRING.getClass().getName();

        System.out.println("Полученное имя для класса нашего объекта: " + className);
        System.out.println("Полученное имя для класса через class: " + classNameFromKey);
        System.out.println("Полученное имя для стандартного класса: " + classNameString);
        System.out.println();

        // Шаг 2
        // Рассмотрим второй способ создания объекта
        try {
            Class classFromName = Class.forName("ru.rzn.sbt.javaschool.reflection.base.TheCat"); //!!! - Имя класса должно быть полностью
            System.out.println("Полученное имя для theCatClass: " + classFromName.getName());

            // System.out.println("Голос по умолчанию: " + classFromName.getVoice());
            TheCat theCatClassInstance = (TheCat) classFromName.newInstance();

            Constructor constructor = classFromName.getConstructor(); //!!! - Новый вариант создания объекта, взамен deprecated метода
            TheCat theCatConstructor = (TheCat) constructor.newInstance();

            System.out.println("Голос по умолчанию: " + theCatClassInstance.getVoice());
            System.out.println("Голос по умолчанию: " + theCatConstructor.getVoice());
            System.out.println();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Шаг 3
        // Получение модификаторов доступа для класса
        int classModifiers = TheCat.class.getModifiers();
        if (Modifier.isPublic(classModifiers)) {
            System.out.println("Класс TheCat имеет модификатор public");
        }
        if (Modifier.isAbstract(classModifiers)) {
            System.out.println("Класс TheCat имеет модификатор abstract");
        }
        if (Modifier.isFinal(classModifiers)) {
            System.out.println("Класс TheCat имеет модификатор final");
        }

        // Шаг 4
        // Получение суперклассов
        Class superclass = TheCat.class.getSuperclass();
        System.out.println("Класс TheCat имеет суперкласс: " + superclass);

        // Шаг 5
        // Получение интерфейсов
        Class[] interfaces = TheCat.class.getInterfaces();
        for (Class cInterface : interfaces) {
            System.out.println("Класс TheCat реализует: " + cInterface.getName());
        }

        // Шаг 6
        // Получение полей класса
        Field[] publicFields = TheCat.class.getFields();
        for (Field field : publicFields) {
            Class fieldType = field.getType();
            System.out.println("Имя поля: " + field.getName());
            System.out.println("Тип поля: " + fieldType.getName());
            System.out.println();
        }

        // Получеине поле класса
        try {
            Field publicField = TheCat.class.getField("NAME");//!!! - Имя поля регистрозависимое
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        // Шаг 7
        // Получение конструктора
        Constructor[] constructors = TheCat.class.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.print("Конструктор: " + constructor.getName() + "(");
            Class[] paramTypes = constructor.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                System.out.print(paramTypes[i].getName());
                if (i < paramTypes.length - 1) System.out.print(", ");
            }
            System.out.println(")");
        }

        // Получение конкретного конструктора
        Class[] paramTypes = new Class[]{String.class};//!!! - Нет конструктора с параметрами String, int
        try {
            Constructor currentConstructor = TheCat.class.getConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Шаг 8
        // Изучение информации о методе
        Method[] methods = TheCat.class.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Имя метода: " + method.getName());
            System.out.println("Возвращаемый тип: " + method.getReturnType().getName());
            Class[] methodParameterTypes = method.getParameterTypes();
            System.out.print("Типы параметров: ");
            for (Class paramType : methodParameterTypes) {
                System.out.print(" " + paramType.getName());
            }
            System.out.println("\n");
        }

        // Шаг 9
        // Получение конкретного метода и его выполнение
        Class[] paramTypesM = new Class[]{String.class};//!!! - Список параметров был неверный
        try {
            Method mSetVoice = TheCat.class.getMethod("setVoice", paramTypesM);
            mSetVoice.invoke(theCat, "ГАВ");//!!! - В качестве объекта надо передавать сам объект, а не Class

            Method mGetVoice = TheCat.class.getMethod("getVoice");
            System.out.println(mGetVoice.invoke(theCat));
            System.out.println();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//
//        Class[] paramTypesM = new Class[]{int.class, String.class};
//        try {
//            Method m = TheCat.class.getMethod("methodA", paramTypesM);
//            m.invoke(TheCat.class, args);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        //  Для всех заданий
        try {
            Class clazz = Class.forName("ru.rzn.sbt.javaschool.reflection.base.TheCat");

            //    Задание. Написать рекурсивную функцию для получения все суперклассов для переданного класса.
            System.out.println("Задание. Написать рекурсивную функцию для получения все суперклассов для переданного класса.");
            getSuperClasses(clazz);
            System.out.println();

            //   Задание. Написать рекурсивную функцию для получения всех интерфейсов класса и суперклассов для переданного класса.
            System.out.println("Задание. Написать рекурсивную функцию для получения всех интерфейсов класса и суперклассов для переданного класса.");
            getInterfacesSuperClasses(clazz);
            System.out.println();

            //  Задание. Получить конкретное поле и его значение.
            System.out.println("Задание. Получить конкретное поле и его значение.");
            Field field = clazz.getDeclaredField("voice");
            field.setAccessible(true);
            System.out.println(field.get(theCat));
            System.out.println();

            //  Задание. Реализовать отображение всех методов с указанием какому классу принадлежит этот метод.
            System.out.println("Задание. Реализовать отображение всех методов с указанием какому классу принадлежит этот метод.");
            getAllMethods(clazz);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public static void getSuperClasses(Class clazz) {
        System.out.println(clazz.getName());
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            getSuperClasses(superClazz);
        }
    }

    public static void getInterfacesSuperClasses(Class clazz) {
        System.out.println(clazz.getName());
        System.out.print("Implements:");
        Class[] interfaces = clazz.getInterfaces();
        for (Class inter : interfaces) {
            System.out.print(" " + inter.getName());
        }
        System.out.println();
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            getSuperClasses(superClazz);
        }
    }

    public static void getAllMethods(Class clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println(method.getDeclaringClass().getName());
            System.out.println("Имя метода: " + method.getName());
            System.out.println("Возвращаемый тип: " + method.getReturnType().getName());
            Class[] methodParameterTypes = method.getParameterTypes();
            System.out.print("Типы параметров: ");
            for (Class paramType : methodParameterTypes) {
                System.out.print(" " + paramType.getName());
            }
            System.out.println("\n");
        }
    }

}
