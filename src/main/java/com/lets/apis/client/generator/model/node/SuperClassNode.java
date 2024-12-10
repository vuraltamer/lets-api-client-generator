package com.lets.apis.client.generator.model.node;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.constants.CallerConstants;
import com.lets.apis.client.generator.model.ImportDetail;
import com.lets.apis.client.generator.util.base.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
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
public class SuperClassNode {
    public Type type; // raw
    public List<SuperClassNode> childList = new ArrayList<>(); //child
    public Set<ImportDetail> imports = new HashSet<>();
    @NonNull
    public String view; // raw<child>
    public String name;

    public SuperClassNode(Type type) {
        this.type = type;
    }

    public static SuperClassNode create(Class clazz) {
        if (Util.isNonControllerClass(clazz.getSuperclass())) {
            return null;
        }
        Type superclassType = clazz.getGenericSuperclass();
        if (superclassType instanceof ParameterizedType) {
            return createFromParameterized((ParameterizedType) superclassType); // parameterized
        }
        return createFromType(clazz.getSuperclass()); // type
    }

    private static SuperClassNode createFromType(Type type) {
        SuperClassNode parameterNode = new SuperClassNode(type);
        parameterNode.setView(setBasicClassView(type));
        parameterNode.setImports(setBasicClassImports(type));
        return parameterNode;
    }

    public static SuperClassNode createFromParameterized(ParameterizedType type) {
        SuperClassNode parameterized = createParameterizedNode(type);
        parameterized.setView(getParameterizedView(parameterized));
        parameterized.setImports(getParameterizedImports(parameterized));
        return parameterized;
    }

    private static Set<ImportDetail> getParameterizedImports(SuperClassNode node) {
        Set<ImportDetail> imports = new HashSet<>();
        imports.add(new ImportDetail(((Class) node.getType())));
        if (node.getChildList().isEmpty()) {
            return imports;
        }
        node.getChildList()
                .forEach(n -> imports.addAll(getParameterizedImports(n)));
        return imports;
    }

    private static String getParameterizedView(SuperClassNode node) {
        String view = CallerConstants.PARAMETERIZED.replace("{RAW}", node.getSimpleName());
        if (node.getChildList().size() == 1) {
            SuperClassNode parameterNode = node.getChildList().get(0);
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

    private static SuperClassNode createParameterizedNode(ParameterizedType type) {
        SuperClassNode parameterNode = new SuperClassNode(type.getRawType());
        Type[] actualTypeArguments = type.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            if (actualTypeArgument instanceof ParameterizedType) {
                SuperClassNode childParameterNode = createParameterizedNode((ParameterizedType) actualTypeArgument);
                parameterNode.childList.add(childParameterNode);
                continue;
            }
            parameterNode.childList.add(new SuperClassNode(actualTypeArgument));
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


    public String getParameterView() {
        return "{VIEW} {NAME}"
                .replace("{VIEW}", this.getView())
                .replace("{NAME}", "this.getName()");
    }

    private Set<Class> getClassNodeClasses(SuperClassNode parameterNode) {
        Set<Class> classes = new HashSet<>();
        if (Util.isArray(parameterNode.getType())) {
            classes.add(((Class<?>) parameterNode.getType()).getComponentType());
        } else {
            classes.add((Class) parameterNode.getType());
        }
        if (parameterNode.getChildList().size() == 0) {
            return classes;
        }
        for (SuperClassNode methodParameterNode : parameterNode.getChildList()) {
            classes.addAll(getClassNodeClasses(methodParameterNode));
        }
        return classes;
    }
}