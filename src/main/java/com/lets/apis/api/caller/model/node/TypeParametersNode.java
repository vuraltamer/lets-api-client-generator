package com.lets.apis.api.caller.model.node;

import com.lets.apis.api.caller.constants.ApiConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TypeParametersNode {
    private List<String> parameters;
    private String view;

    public static TypeParametersNode create(Class clazz) {
        TypeVariable[] clazzTypeParameters = clazz.getTypeParameters();
        TypeParametersNode typeParametersNode = new TypeParametersNode();
        typeParametersNode.setParameters(getParameters(clazzTypeParameters));
        typeParametersNode.setView(getView(typeParametersNode.getParameters()));
        return typeParametersNode;
    }

    private static List<String> getParameters(TypeVariable[] clazzTypeParameters) {
        return Arrays.stream(clazzTypeParameters)
                .map(typeVariable -> typeVariable.getName())
                .collect(Collectors.toList());
    }

    private static String getView(List<String> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return ApiConstants.EMPTY;
        }
        return new String("<{PARAMETERS}>")
                .replace("{PARAMETERS}",
                        parameters.stream()
                                .map(parameter -> parameter + " extends Object")
                                .collect(Collectors.joining(", "))

                );
    }
}