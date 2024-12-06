package com.lets.apis.api.caller.model.node;

import com.lets.apis.api.caller.constants.ApiConstants;
import com.lets.apis.api.caller.constants.CallerConstants;
import com.lets.apis.api.caller.model.ImportDetail;
import com.lets.apis.api.caller.util.base.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ParameterNode {
    public Type type; // raw
    public List<ParameterNode> childList = new ArrayList<>(); //child
    private Set<ImportDetail> imports = new HashSet<>();
    public List<AnnotationNode> annotationNodes;
    public String view; // raw<child>
    public String name;

    public ParameterNode(Type type) {
        this.type = type;
    }

    public static List<ParameterNode> create(Method method) {
        List<ParameterNode> parameterTypes = new ArrayList<>();
        for (Parameter parameter : method.getParameters()) {
            parameterTypes.add(create(parameter));
        }
        return parameterTypes;
    }

    private static ParameterNode create(Parameter parameter) {
        ParameterNode parameterNode = createParameterNode(parameter);
        parameterNode.setAnnotationNodes(AnnotationNode.create(parameter));
        parameterNode.setName(parameter.getName());
        addAnnotationImports(parameterNode);
        return parameterNode;
    }

    public static ParameterNode createParameterNode(Parameter parameter) {
        if (parameter.getParameterizedType() instanceof ParameterizedType) {
            return createFromParameterized((ParameterizedType) parameter.getParameterizedType()); // parameterized
        }
        return createFromType(parameter.getType()); // type
    }

    public static ParameterNode createFromType(Type type) {
        ParameterNode parameterNode = new ParameterNode(type);
        parameterNode.setView(setBasicClassView(type));
        parameterNode.getImports().addAll(setBasicClassImports(type));
        return parameterNode;
    }

    public static ParameterNode createFromParameterized(ParameterizedType type) {
        ParameterNode parameterized = createParameterizedNode(type);
        parameterized.setView(getParameterizedView(parameterized));
        parameterized.getImports().addAll(getParameterizedImports(parameterized));
        return parameterized;
    }

    private static Set<ImportDetail> getParameterizedImports(ParameterNode node) {
        Set<ImportDetail> imports = new HashSet<>();
        if (!(node.getType() instanceof Class<?>)) {
            return imports;
        }
        imports.add(new ImportDetail((Class) node.getType()));
        Annotation[] annotations = ((Class<?>) node.getType()).getAnnotations();
        if (annotations.length > 0) {
            imports.addAll(getParameterizedAnnotationImports(annotations));
        }
        if (!node.getChildList().isEmpty()) {
            node.getChildList()
                    .forEach(childNode -> imports.addAll(getParameterizedImports(childNode)));
        }
        return imports;
    }

    private static Set<ImportDetail> getParameterizedAnnotationImports(Annotation[] annotations) {
        Set<AnnotationNode> annotationNodes = Arrays.stream(annotations)
                .map(annotation -> AnnotationNode.create(annotation))
                .collect(Collectors.toSet());
        return annotationNodes.stream()
                        .flatMap(annotationNode -> annotationNode.getImports().stream())
                        .collect(Collectors.toSet());
    }

    private static void addAnnotationImports(ParameterNode parameterNode) {
        parameterNode.getImports().addAll(parameterNode.getAnnotationNodes().stream()
                .flatMap(annotationNode -> annotationNode.getImports().stream())
                .collect(Collectors.toSet()));
    }

    private static String getParameterizedView(ParameterNode node) {
        String view = CallerConstants.PARAMETERIZED.replace("{RAW}", node.getSimpleName());
        if (node.getChildList().size() == 1) {
            ParameterNode parameterNode = node.getChildList().get(0);
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

    private static ParameterNode createParameterizedNode(ParameterizedType type) {
        ParameterNode parameterNode = new ParameterNode(type.getRawType());
        Type[] actualTypeArguments = type.getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            if (actualTypeArgument instanceof ParameterizedType) {
                ParameterNode childParameterNode = createParameterizedNode((ParameterizedType) actualTypeArgument);
                parameterNode.childList.add(childParameterNode);
                continue;
            }
            parameterNode.childList.add(new ParameterNode(actualTypeArgument));
        }
        return parameterNode;
    }

    private static String setBasicClassView(Type type) {
        return ((Class)type).getSimpleName();
    }

    private static Set<ImportDetail> setBasicClassImports(Type type) {
        Set<ImportDetail> imports = new HashSet<>();
        if (!Util.isPrimitive((Class) type)) {
            imports.add(new ImportDetail((Class) type));
        }
        return imports;
    }

    public String getSimpleName() {
        return this.getType() instanceof Class<?>
                ? ((Class) this.getType()).getSimpleName()
                : this.getType().getTypeName();
    }

    public Set<Class> getClassNodeClasses() {
        return getClassNodeClasses(this);
    }

    public String getParameterView() {
        return "{VIEW} {NAME}"
                .replace("{VIEW}", getAnnotatedParameterView())
                .replace("{NAME}", this.getName());
    }

    private String getAnnotatedParameterView() {
        return "{ANNOTATIONS} {PARAMETER}"
                .replace("{PARAMETER}", this.getView())
                .replace("{ANNOTATIONS}", ObjectUtils.isEmpty(this.getAnnotationNodes())
                        ? ApiConstants.EMPTY
                        : this.getAnnotationNodes().stream()
                                .map(AnnotationNode::getView)
                                .collect(Collectors.joining(", ")));
    }

    private Set<Class> getClassNodeClasses(ParameterNode parameterNode) {
        Set<Class> classes = new HashSet<>();
        if (!(parameterNode.getType() instanceof Class<?>)) {
            return classes;
        }
        classes.add((Class) parameterNode.getType());
        if (parameterNode.getChildList().size() == 0) {
            return classes;
        }
        for (ParameterNode methodParameterNode : parameterNode.getChildList()) {
            classes.addAll(getClassNodeClasses(methodParameterNode));
        }
        return classes;
    }
}