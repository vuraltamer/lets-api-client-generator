package com.lets.apis.api.caller.model.node;

import com.lets.apis.api.caller.constants.ApiConstants;
import com.lets.apis.api.caller.model.ImportDetail;
import com.lets.apis.api.caller.util.base.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationNode {
    private Type type;
    private String name;
    private String fullName;
    private Set<ImportDetail> imports = new HashSet<>();
    private HashMap<String, Object> values = new LinkedHashMap<>();
    private String view;

    public static List<AnnotationNode> create(Parameter parameter) {
        return Arrays.stream(parameter.getAnnotations())
                .map(annotation -> create(annotation))
                .collect(Collectors.toList());
    }

    public static AnnotationNode create(Annotation annotation) {
        AnnotationNode annotationNode = new AnnotationNode();
        annotationNode.setFullName(annotation.annotationType().getName());
        annotationNode.setName(annotation.annotationType().getSimpleName());
        annotationNode.setType(annotation.getClass());
        setValuesAndImports(annotationNode, annotation);
        annotationNode.setView(getView(annotationNode));
        return annotationNode;
    }

    private static void setValuesAndImports(AnnotationNode annotationNode, Annotation annotation) {
        annotationNode.getImports().add(new ImportDetail(annotation.annotationType()));
        Method[] methods = annotation.annotationType().getDeclaredMethods();
        for (Method method : methods) {
            try {
                Object value = method.invoke(annotation);
                annotationNode.getValues().put(method.getName(), value);
                annotationNode.getImports().add(new ImportDetail(Util.getClass(value.getClass())));
            } catch (Exception e) {
            }
        }
    }

    private static String getView(AnnotationNode annotationNode) {
        return "@{ANNOTATION}({PARAMETERS})"
                .replace("{ANNOTATION}", annotationNode.getName())
                .replace("{PARAMETERS}", getAnnotationParameters(annotationNode.getValues()));
    }

    private static String getAnnotationParameters(HashMap<String, Object> values) {
        if (values.size() == 0) {
            return ApiConstants.EMPTY;
        }
        return values.keySet().stream()
                .filter(key -> filterAnnotation(values.get(key)))
                .map(key -> "{KEY} = {VALUE}"
                        .replace("{KEY}", key)
                        .replace("{VALUE}", getAnnotationValue(values.get(key))))
                .collect(Collectors.joining(", "));
    }

    private static boolean filterAnnotation(Object value) {
        if (!value.getClass().isArray()) {
            return !ApiConstants.EMPTY.equals(value.toString());
        }
        return Array.getLength(value) != 0;
    }

    private static String getAnnotationValue(Object value) {
        if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            String replaceParam = array.length == 1 ? "{VALUE}" : "{{VALUE}}";
            return replaceParam
                    .replace("{VALUE}", Arrays.stream(array)
                                                .map(element -> element instanceof String ? "\"" + element + "\"" : element.toString())
                                                .collect(Collectors.joining(", "))
                    );
        }
        return value instanceof String
                ? "\"{VALUE}\"".replace("{VALUE}", value.toString())
                : value.toString();
    }
}