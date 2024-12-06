package com.lets.api.caller.util.base;

import com.lets.api.caller.constants.ApiConstants;
import com.lets.api.caller.properties.PropertyReader;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static com.lets.api.caller.constants.CallerConstants.BASE_PATH;

public class Util {

    private static final String REMOVED_PATH = PropertyReader.properties().getScanPackage() + "." ;

    public static boolean isNonControllerClass(Class clazz) {
        return clazz.getPackageName().startsWith("java.") ||
                clazz.getPackageName().startsWith("javax.") ||
                clazz.getPackageName().startsWith("org.spring") ||
                clazz.getPackageName().startsWith("lombok") ||
                clazz.getPackageName().startsWith("org.dozer");
    }

    public static String getPackageName(Class clazz) {
        if (isNonControllerClass(clazz)) {
            return clazz.getPackageName();
        }
        if (clazz.isMemberClass()) {
            return BASE_PATH + clazz.getDeclaringClass().getCanonicalName()
                    .toLowerCase()
                    .replaceFirst(REMOVED_PATH, ApiConstants.EMPTY);
        }

        return BASE_PATH + clazz.getPackageName().replaceFirst(REMOVED_PATH, ApiConstants.EMPTY);
    }

    public static String getFullName(Class clazz) {
        if (isNonControllerClass(clazz)) {
            return clazz.getCanonicalName();
        }

        return getPackageName(clazz) + "." + clazz.getSimpleName();
    }

    public static String getControllerFullName(Class clazz) {
        if (isNonControllerClass(clazz)) {
            return clazz.getPackageName();
        }
        return BASE_PATH + clazz.getName()
                .replaceFirst(REMOVED_PATH, ApiConstants.EMPTY)
                .replaceFirst("Controller", "Client");
    }

    public static String getControllerName(Class clazz) {
        return clazz.getSimpleName()
                .replace("Controller", "Client");
    }

    public static boolean isPrimitive(Class clazz) {
        return clazz.isPrimitive();
    }

    public static boolean isStatic(Class clazz) {
        return Modifier.isStatic(clazz.getModifiers());
    }

    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    public static Class getClass(Class clazz) {
        return clazz.getComponentType() != null
                ? clazz.getComponentType()
                : clazz;
    }

    public static boolean isEnum(Class clazz) {
        return clazz.isEnum();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isEmpty(List<String> list) {
        return list == null || list.isEmpty();
    }
}