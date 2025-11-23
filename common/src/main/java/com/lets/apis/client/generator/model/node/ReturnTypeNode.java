package com.lets.apis.client.generator.model.node;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.constants.CallerConstants;
import com.lets.apis.client.generator.model.ImportDetail;
import com.lets.apis.client.generator.util.base.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ReturnTypeNode {
    public Type type; // raw
    public List<ReturnTypeNode> childList = new ArrayList<>(); //child
    public Set<ImportDetail> imports = new HashSet<>();
    public String view; // raw<child>

    public ReturnTypeNode(Type type) {
        this.type = type;
    }

    public static ReturnTypeNode create(Method method) {
        if (method.getGenericReturnType() instanceof ParameterizedType) {
            return createFromParameterized((ParameterizedType) method.getGenericReturnType()); // parameterized
        }
        return createFromType(method.getReturnType()); // type
    }

    private static ReturnTypeNode createFromType(Type type) {
        ReturnTypeNode parameterNode = new ReturnTypeNode(type);
        parameterNode.setView(setBasicClassView(type));
        parameterNode.setImports(setBasicClassImports(type));
        return parameterNode;
    }

    public static ReturnTypeNode createFromParameterized(ParameterizedType type) {
        ReturnTypeNode parameterized = createParameterizedNode(type);
        parameterized.setView(getParameterizedView(parameterized));
        parameterized.setImports(getParameterizedImports(parameterized));
        return parameterized;
    }

    private static Set<ImportDetail> getParameterizedImports(ReturnTypeNode node) {
        Set<ImportDetail> imports = new HashSet<>();
        imports.add(new ImportDetail(Util.getClass((Class) node.getType())));
        if (node.getChildList().isEmpty()) {
            return imports;
        }
        node.getChildList()
                .forEach(childNode -> imports.addAll(getParameterizedImports(childNode)));
        return imports;
    }

    private static String getParameterizedView(ReturnTypeNode node) {
        String view = CallerConstants.PARAMETERIZED.replace("{RAW}", node.getSimpleName());
        if (node.getChildList().size() == 1) {
            ReturnTypeNode parameterNode = node.getChildList().get(0);
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

    private static ReturnTypeNode createParameterizedNode(ParameterizedType type) {
        ReturnTypeNode parameterNode = new ReturnTypeNode(type.getRawType());
        Type[] actualTypeArguments = type.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            if (actualTypeArgument instanceof ParameterizedType) {
                ReturnTypeNode childParameterNode = createParameterizedNode((ParameterizedType) actualTypeArgument);
                parameterNode.childList.add(childParameterNode);
                continue;
            }
            parameterNode.childList.add(new ReturnTypeNode(actualTypeArgument));
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
        return ((Class) this.getType()).getSimpleName();
    }

    public Set<Class> getClassNodeClasses() {
        return getClassNodeClasses(this);
    }

    private Set<Class> getClassNodeClasses(ReturnTypeNode parameterNode) {
        Set<Class> classes = new HashSet<>();
        if (Util.isArray(parameterNode.getType())) {
            classes.add(((Class<?>) parameterNode.getType()).getComponentType());
        } else {
            classes.add((Class) parameterNode.getType());
        }
        if (parameterNode.getChildList().size() == 0) {
            return classes;
        }
        for (ReturnTypeNode methodParameterNode : parameterNode.getChildList()) {
            classes.addAll(getClassNodeClasses(methodParameterNode));
        }
        return classes;
    }
}