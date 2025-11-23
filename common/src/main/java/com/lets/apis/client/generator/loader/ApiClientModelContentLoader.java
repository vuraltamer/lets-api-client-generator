package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.model.ApiClientModelDetail;
import com.lets.apis.client.generator.template.TemplateReader;
import com.lets.apis.client.generator.util.base.Util;

public class ApiClientModelContentLoader {

    public static String load(ApiClientModelDetail apiClientModelDetail) {
        if (Util.isEnum(apiClientModelDetail.getClazz())) {
            return loadEnumContent(apiClientModelDetail);
        }
        return loadClassContent(apiClientModelDetail);
    }

    private static String loadClassContent(ApiClientModelDetail apiClientModelDetail) {
        return new String(TemplateReader.content("api-model"))
                        .replace("{PACKAGE}", apiClientModelDetail.getPackageName())
                        .replace("{IMPORTS}", apiClientModelDetail.getImportsContent())
                        .replace("{CLASS_NAME}", apiClientModelDetail.getClassName())
                        .replace("{TYPE_PARAMETERS}", apiClientModelDetail.getTypeParameters().getView())
                        .replace("{EXTENDS}", apiClientModelDetail.getExtendsContent())
                        .replace("{VARIABLES}", apiClientModelDetail.getDeclaredFieldsContent());
    }

    private static String loadEnumContent(ApiClientModelDetail apiClientModelDetail) {
        return new String(TemplateReader.content("api-enum"))
                        .replace("{PACKAGE}", apiClientModelDetail.getPackageName())
                        .replace("{ENUM_NAME}", apiClientModelDetail.getClassName())
                        .replace("{VARIABLES}", apiClientModelDetail.getEnumFieldContent());
    }
}