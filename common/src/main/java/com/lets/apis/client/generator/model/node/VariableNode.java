package com.lets.apis.client.generator.model.node;

import com.lets.apis.client.generator.util.base.Util;
import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.model.ImportDetail;
import com.lets.apis.client.generator.constants.CallerConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class VariableNode {
    public Type type; // raw
    public List<VariableNode> childList = new ArrayList<>(); //child
    public Set<ImportDetail> imports = new HashSet<>();
    public String view; // raw<child>
    public String name;

    public VariableNode(Type type) {
        this.type = type;
    }

    public static VariableNode create(Field field) {
        if (field.getGenericType() instanceof ParameterizedType) {
            VariableNode parameterNode = createFromParameterized((ParameterizedType) field.getGenericType()); // parameterized
            parameterNode.setName(field.getName());
            return parameterNode;
        }
        if (field.getGenericType() instanceof TypeVariable) {
            VariableNode parameterNode = createFromType(field.getType()); // parameterized
            parameterNode.setName(field.getName());
            parameterNode.setView(field.getGenericType().getTypeName());
            return parameterNode;
        }
        VariableNode parameterNode = createFromType(field.getType()); // type
        parameterNode.setName(field.getName());
        parameterNode.setView(field.getType().getSimpleName());
        return parameterNode;
    }

    private static VariableNode createFromType(Type type) {
        VariableNode parameterNode = new VariableNode(type);
        parameterNode.setView(setBasicClassView(type));
        parameterNode.setImports(setBasicClassImports(type));
        return parameterNode;
    }

    public static VariableNode createFromParameterized(ParameterizedType type) {
        VariableNode parameterized = createParameterizedNode(type);
        parameterized.setView(getParameterizedView(parameterized));
        parameterized.setImports(getParameterizedImports(parameterized));
        return parameterized;
    }

    private static Set<ImportDetail> getParameterizedImports(VariableNode node) {
        Set<ImportDetail> imports = new HashSet<>();
        if (!(node.getType() instanceof Class<?>)) {
            return imports;
        }
        imports.add(new ImportDetail((Class) node.getType()));
        if (((Class<?>) node.getType()).getAnnotations().length > 0) {
            Annotation[] annotations = ((Class<?>) node.getType()).getAnnotations();
            imports.addAll(
                    Arrays.stream(annotations)
                            .map(annotation -> new ImportDetail(annotation.annotationType()))
                            .collect(Collectors.toSet())
            );
        }
        if (!node.getChildList().isEmpty()) {
            node.getChildList()
                    .forEach(n -> imports.addAll(getParameterizedImports(n)));
        }
        return imports;
    }

    private static String getParameterizedView(VariableNode node) {
        String view = CallerConstants.PARAMETERIZED.replace("{RAW}", node.getSimpleName());
        if (node.getChildList().size() == 1) {
            VariableNode parameterNode = node.getChildList().get(0);
            view = (view.replace("{CONTENT}", getParameterizedView(parameterNode)));
        } else if (node.getChildList().size() > 1) {
            String multiContent = node.getChildList().stream()
                    .map(childNode -> {
                        if (childNode.getChildList().size() > 0) {
                            return getParameterizedView(childNode);
                        } else {
                            return childNode.getSimpleName();
                        }
                    })
                    .collect(Collectors.joining(", "));
            view = (view.replace("{CONTENT}", multiContent));
        } else {
            view = (view.replace("<{CONTENT}>", ApiConstants.EMPTY));
        }
        return view;
    }

    private static VariableNode createParameterizedNode(ParameterizedType type) {
        VariableNode parameterNode = new VariableNode(type.getRawType());
        Type[] actualTypeArguments = type.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            if (actualTypeArgument instanceof ParameterizedType) {
                VariableNode childParameterNode = createParameterizedNode((ParameterizedType) actualTypeArgument);
                parameterNode.childList.add(childParameterNode);
                continue;
            }
            parameterNode.childList.add(new VariableNode(actualTypeArgument));
        }
        return parameterNode;
    }

    private static String setBasicClassView(Type type) {
        return ((Class)type).getSimpleName();
    }

    private static Set<ImportDetail> setBasicClassImports(Type type) {
        Set<ImportDetail> imports = new HashSet<>();
        if (!Util.isPrimitive((Class) type)) {
            if (Util.isArray(type)) {
                imports.add(new ImportDetail(((Class<?>) type).getComponentType()));
            } else {
                imports.add(new ImportDetail((Class) type));
            }
        }
        return imports;
    }

    public String getSimpleName() {
        return this.getType() instanceof Class
                ? ((Class) this.getType()).getSimpleName()
                : this.getType().getTypeName();
    }

    public Set<Class> getClassNodeClasses() {
        return getClassNodeClasses(this);
    }


    public String getParameterView() {
        return "{VIEW} {NAME}"
                .replace("{VIEW}", this.getView())
                .replace("{NAME}", "this.getName()");
    }

    private Set<Class> getClassNodeClasses(VariableNode parameterNode) {
        Set<Class> classes = new HashSet<>();
        if (Util.isArray(parameterNode.getType())) {
            classes.add(((Class<?>) parameterNode.getType()).getComponentType());
        } else {
            classes.add((Class) parameterNode.getType());
        }
        if (parameterNode.getChildList().size() == 0) {
            return classes;
        }
        for (VariableNode methodParameterNode : parameterNode.getChildList()) {
            classes.addAll(getClassNodeClasses(methodParameterNode));
        }
        return classes;
    }
}