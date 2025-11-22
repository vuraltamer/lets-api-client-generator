package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.model.ApiClientDetail;
import com.lets.apis.client.generator.model.ApiClientMethodDetail;
import com.lets.apis.client.generator.model.node.ParameterNode;
import com.lets.apis.client.generator.properties.ApplicationPropertyReader;
import com.lets.apis.client.generator.constants.CallerConstants;
import com.lets.apis.client.generator.template.TemplateReader;

import java.util.List;
import java.util.stream.Collectors;

public class ApiClientContentLoader {

    public static String create(ApiClientDetail apiClientDetail) {
        return TemplateReader.content("feign-client")
                .replace("{PACKAGE}", apiClientDetail.getPackageName())
                .replace("{API_NAME}", getFeignApiName(apiClientDetail.getClassName()))
                .replace("{FEIGN_URL}", getFeignUrl())
                .replace("{PATH}", apiClientDetail.getPathView())
                .replace("{IMPORTS}", apiClientDetail.getImports())
                .replace("{CLASS_NAME}", apiClientDetail.getClassName())
                .replace("{METHODS}", loadMethods(apiClientDetail));
    }

    private static String getFeignApiName(String className) {
        return getFeignApiName() + "-" + className
                                        .replaceAll("([a-z])([A-Z])", "$1-$2")
                                        .toLowerCase();
    }

    private static String  getFeignUrl() {
        return CallerConstants.BASE_PATH + "." + getFeignApiName();
    }

    private static String loadMethods(ApiClientDetail apiClientDetail) {
        return apiClientDetail.getMethods().stream()
                .map(method -> loadMethod(method))
                .collect(Collectors.joining("\n"));
    }

    private static String loadMethod(ApiClientMethodDetail method) {
        return """
                    {ANNOTATION}
                    {RETURN_TYPE} {NAME}({PARAMETERS});
                """
                .replace("{ANNOTATION}", method.getAnnotation().getView())
                .replace("{RETURN_TYPE}", method.getReturnType().getView())
                .replace("{NAME}", method.getMethod().getName())
                .replace("{PARAMETERS}", loadParameters(method.getParameterTypes()));
    }

    private static String loadParameters(List<ParameterNode> parameterTypes) {
        return parameterTypes.stream()
                .map(ParameterNode::getParameterView)
                .collect(Collectors.joining(", "));
    }

    private static String getFeignApiName() {
        return ApplicationPropertyReader.properties().getClientName();
    }
}
