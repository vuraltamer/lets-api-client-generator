package com.lets.apis.client.generator.loader;

import com.lets.apis.client.generator.model.ControllerDetail;
import com.lets.apis.client.generator.model.ControllerMethodDetail;
import com.lets.apis.client.generator.model.node.ParameterNode;
import com.lets.apis.client.generator.properties.PropertyReader;
import com.lets.apis.client.generator.constants.CallerConstants;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerContentLoader {

    private static final String FEIGN_API_NAME = PropertyReader.properties().getApiName();

    public static String create(ControllerDetail controllerDetail) {
        return new String(CallerConstants.FEIGN_CLASS_CONTENT)
                        .replace("{PACKAGE}", controllerDetail.getPackageName())
                        .replace("{FEIGN_URL}", getFeignUrl())
                        .replace("{PATH}", controllerDetail.getPathView())
                        .replace("{IMPORTS}", controllerDetail.getImports())
                        .replace("{CLASS_NAME}", controllerDetail.getClassName())
                        .replace("{METHODS}", loadMethods(controllerDetail));
    }

    private static String  getFeignUrl() {
        return CallerConstants.BASE_PATH + FEIGN_API_NAME;
    }

    private static String loadMethods(ControllerDetail controllerDetail) {
        return controllerDetail.getMethods().stream()
                .map(method -> loadMethod(method))
                .collect(Collectors.joining("\n"));
    }

    private static String loadMethod(ControllerMethodDetail method) {
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
}
