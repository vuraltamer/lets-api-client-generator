package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.constants.CallerConstants;
import com.lets.apis.client.generator.model.ControllerModelDetail;
import com.lets.apis.client.generator.util.base.Util;

public class ControllerModelContentLoader {

    public static String load(ControllerModelDetail controllerModelDetail) {
        if (Util.isEnum(controllerModelDetail.getClazz())) {
            return loadEnumContent(controllerModelDetail);
        }
        return loadClassContent(controllerModelDetail);
    }

    private static String loadClassContent(ControllerModelDetail controllerModelDetail) {
        return new String(CallerConstants.API_CLASS_CONTENT)
                        .replace("{PACKAGE}", controllerModelDetail.getPackageName())
                        .replace("{IMPORTS}", controllerModelDetail.getImportsContent())
                        .replace("{CLASS_NAME}", controllerModelDetail.getClassName())
                        .replace("{TYPE_PARAMETERS}", controllerModelDetail.getTypeParameters().getView())
                        .replace("{EXTENDS}", controllerModelDetail.getExtendsContent())
                        .replace("{VARIABLES}", controllerModelDetail.getDeclaredFieldsContent());
    }

    private static String loadEnumContent(ControllerModelDetail controllerModelDetail) {
        return new String(CallerConstants.API_ENUM_CONTENT)
                        .replace("{PACKAGE}", controllerModelDetail.getPackageName())
                        .replace("{ENUM_NAME}", controllerModelDetail.getClassName())
                        .replace("{VARIABLES}", controllerModelDetail.getEnumFieldContent());
    }
}
