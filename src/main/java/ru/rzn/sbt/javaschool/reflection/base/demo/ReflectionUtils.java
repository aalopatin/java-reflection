package ru.rzn.sbt.javaschool.reflection.base.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Класс, предоставляющий общие функции для использования рефлексии.
 */
public class ReflectionUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * Метод, получающий полный список полей объекта, включая его суперклассы.
     *
     * @param fields набор уже обнаруженых полей объекта
     * @param type   класс объекта
     * @return полный список полей объекта
     */
    public static List<Field> getAllObjectFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            fields = getAllObjectFields(fields, type.getSuperclass());
        }
        return fields;
    }

    /**
     * Метод, получающий полный список полей объекта, включая его суперклассы.
     *
     * @param clazz класс объекта
     * @return полный список полей объекта
     */
    public static List<Field> getAllObjectFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllObjectFields(clazz.getSuperclass()));
        }
        return fields;
    }

    /**
     * Метод, обходящий публичные поля целевого объекта и генерирующий HashMap пар ключ-значение, где ключ - полный путь имени класса + символ разделителя (.) + имя поля, а
     * значение - не-null значение поля, полученное стандартным геттером.
     *
     * @param targetObj объект для обхода публичных полей
     * @return заполненный HashMap
     * @see #getDefaultGetterNameByField(Field field)
     * @see #getMethodIfPresent(String, Object, Class[])
     */
    public static HashMap<String, Object> createValueMapFromObject(Object targetObj) {
        HashMap<String, Object> map = new LinkedHashMap<String, Object>();
        for (Field field : getAllObjectFields(targetObj.getClass())) {
            Method method = getMethodIfPresent(getDefaultGetterNameByField(field), targetObj);
            if (null != method) {
                if (Modifier.isPublic(method.getModifiers())) {
                    String key = targetObj.getClass().getName() + "." + field.getName();
                    Object value = getValueFromCallingMethod(method, targetObj);
                    if (null != value) {
                        map.put(key, value);
                    }
                }
            }
        }
        return map;
    }

    /**
     * Метод, возвращающий объект метода, запрашиваемого по имени. Если метод не представлен в объекте - возвращается null, исключение игнорируется.
     *
     * @param methodName     имя запрашиваемого метода
     * @param object         объект, в котором должен присутствовать метод
     * @param parameterTypes параметры (необязательно)
     * @return объект типа Method
     */
    public static Method getMethodIfPresent(String methodName, Object object, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = object.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            LOG.debug("При получении метода {} на объекте {} возникла ошибка: {}, ошибку игнорируем", methodName, object.getClass().getName(), e.getLocalizedMessage());
        }
        return method;
    }

    /**
     * Метод, возвращающий результат выполнения метода, переданного в качестве параметра, на объекте, также переданном в качестве параметра. Если в процессе получения
     * значения возникает ошибка, возвращается null.
     *
     * @param m   объект метода
     * @param obj объект, на котором нужно получить значение метода
     * @return результарующий объект типа Object
     */
    public static Object getValueFromCallingMethod(Method m, Object obj) {
        Object resultObj = null;
        try {
            resultObj = m.invoke(obj);
        } catch (IllegalAccessException e) {
            LOG.debug("При получении значения метода {} на объекте {} возникла ошибка: {}, ошибку игнорируем", m.getName(), obj.getClass().getName(), e.getLocalizedMessage());
        } catch (InvocationTargetException e) {
            LOG.debug("При получении значения метода {} на объекте {} возникла ошибка: {}, ошибку игнорируем", m.getName(), obj.getClass().getName(), e.getLocalizedMessage());
        }
        return resultObj;
    }

    /**
     * Метод, вызывающий выполнение метода, переданного в качестве параметра, на объекте, также переданном в качестве параметра.
     *
     * @param method объект метода
     * @param obj    объект, на котором нужно получить значение метода
     */
    public static void invokeMethod(Method method, Object obj, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            LOG.debug("При выполнении метода {} на объекте {} возникла ошибка: {}, ошибку игнорируем", method.getName(), obj.getClass().getName(), e.getLocalizedMessage());
        } catch (InvocationTargetException e) {
            LOG.debug("При выполнении метода {} на объекте {} возникла ошибка: {}, ошибку игнорируем", method.getName(), obj.getClass().getName(), e.getLocalizedMessage());
        }
    }

    /**
     * @see #getDefaultGetterNameByField(String fieldName)
     */
    public static String getDefaultGetterNameByField(Field field) {
        return (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class))
                ? getDefaultGetterNameByBooleanField(field.getName())
                : getDefaultGetterNameByField(field.getName());
    }

    /**
     * Получение имени геттера "по умолчанию" для конкретного поля. Формируется как статичный префикс get + имя поля с прописной буквы.
     *
     * @param fieldName имя поля
     * @return строка, содержащая имя геттера
     * @see #createValueMapFromObject (Object targetObj)
     */
    public static String getDefaultGetterNameByField(String fieldName) {
        StringBuilder sb = new StringBuilder("get");
        sb.append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
        return sb.toString();
    }

    /**
     * Получение имени геттера "по умолчанию" для конкретного поля. Для типа Boolean формируется как статичный префикс is + имя поля с прописной буквы.
     *
     * @param fieldName имя поля
     * @return строка, содержащая имя геттера
     * @see #createValueMapFromObject (Object targetObj)
     */
    public static String getDefaultGetterNameByBooleanField(String fieldName) {
        StringBuilder sb = new StringBuilder("is");
        sb.append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
        return sb.toString();
    }

    /**
     * Метод получения наименования поля по его ключу. В нашей текущей реализации ключ формируется как полное имя класса плюс символ разделителя (.) плюс имя поля.
     *
     * @param key полное имя поля, например ru.sbt.integration.web.srr.reflection.LOG
     * @return возвращаемое значение имени поля - LOG
     * @see #createValueMapFromObject(Object targetObj)
     */
    public static String getFieldNameFromMapKey(String key) {
        return key.substring(key.lastIndexOf(".") + 1);
    }
}
