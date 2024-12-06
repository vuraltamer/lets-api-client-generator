package com.lets.apis.api.caller.model;

import com.lets.apis.api.caller.model.node.AnnotationNode;
import com.lets.apis.api.caller.model.node.ParameterNode;
import com.lets.apis.api.caller.model.node.ReturnTypeNode;
import lombok.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerMethodDetail {
    private Method method;
    private AnnotationNode annotation;
    private ReturnTypeNode returnType;
    private List<ParameterNode> parameterTypes;

    public Set<Class> getClassNodeClasses() {
        Set<Class> methodClasses = getClassNodeClasses(this.returnType);
        this.parameterTypes.stream()
                .forEach(e -> methodClasses.addAll(getClassNodeClasses(e)));
        return methodClasses;

    }

    public Set<ImportDetail> getImports() {
        Set<ImportDetail> importClasses = new HashSet<>();
        importClasses.addAll(this.getAnnotation().getImports());
        importClasses.addAll(this.getReturnType().getImports());
        importClasses.addAll(
                this.getParameterTypes().stream()
                    .flatMap(parameterNode -> parameterNode.getImports().stream())
                    .collect(Collectors.toSet())
        );
        return importClasses;
    }

    private Set<Class> getClassNodeClasses(ReturnTypeNode parameterNode) {
        return parameterNode.getClassNodeClasses();
    }

    private Set<Class> getClassNodeClasses(ParameterNode parameterNode) {
        return parameterNode.getClassNodeClasses();
    }
}