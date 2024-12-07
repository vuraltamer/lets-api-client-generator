package com.lets.apis.client.generator.util.base;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.properties.PropertyReader;
import com.lets.apis.client.generator.constants.CallerConstants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

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
            return CallerConstants.BASE_PATH + clazz.getDeclaringClass().getCanonicalName()
                    .toLowerCase()
                    .replaceFirst(REMOVED_PATH, ApiConstants.EMPTY);
        }

        return CallerConstants.BASE_PATH + clazz.getPackageName().replaceFirst(REMOVED_PATH, ApiConstants.EMPTY);
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
        return CallerConstants.BASE_PATH + clazz.getName()
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

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}