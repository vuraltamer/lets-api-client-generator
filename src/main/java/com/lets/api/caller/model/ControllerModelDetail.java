package com.lets.api.caller.model;

import com.lets.api.caller.constants.ApiConstants;
import com.lets.api.caller.loader.ControllerModelContentLoader;
import com.lets.api.caller.model.node.SuperClassNode;
import com.lets.api.caller.model.node.TypeParametersNode;
import com.lets.api.caller.model.node.VariableNode;
import com.lets.api.caller.util.base.Util;
import lombok.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.lets.api.caller.constants.CallerConstants.MODEL_IMPORTS;
import static com.lets.api.caller.util.base.Util.isStatic;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerModelDetail {
    private Class clazz;
    private SuperClassNode superClass;
    private TypeParametersNode typeParameters;
    private List<VariableNode> variables;
    private String packageName;
    private String className;
    private String fullName;
    private String content;

    public ControllerModelDetail(Class clazz) {
        this.clazz  = clazz;
        this.className = clazz.getSimpleName();
        this.packageName = Util.getPackageName(clazz);
        this.fullName = Util.getFullName(clazz);
    }

    public static ControllerModelDetail create(Class clazz) {
        ControllerModelDetail controllerModelDetail = new ControllerModelDetail(clazz);
        controllerModelDetail.setTypeParameters(TypeParametersNode.create(clazz));
        controllerModelDetail.setSuperClass(SuperClassNode.create(clazz));
        controllerModelDetail.setVariables(createVariables(clazz));
        controllerModelDetail.setContent(ControllerModelContentLoader.load(controllerModelDetail));
        return controllerModelDetail;
    }

    private static List<VariableNode> createVariables(Class clazz) {
        List<VariableNode> variables = new ArrayList<>();
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (isStatic(declaredField)) {
                continue;
            }
            VariableNode parameterNode = VariableNode.create(declaredField);
            variables.add(parameterNode);
        }
        return variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControllerModelDetail that = (ControllerModelDetail) o;
        return Objects.equals(className, that.className) && Objects.equals(packageName, that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, packageName);
    }

    public String getImportsContent() {
        Set<ImportDetail> imports = ImportDetail.create(MODEL_IMPORTS);
        imports.addAll(getVariableImports());
        return imports.stream()
                .sorted(Comparator.comparing(ImportDetail::getFullName))
                .map(importDetail -> "import {IMPORT};\n".replace("{IMPORT}", importDetail.getFullName()))
                .collect(Collectors.joining());
    }

    private Set<ImportDetail> getVariableImports() {
        return this.variables.stream()
                .flatMap(variable -> variable.getImports().stream())
                .collect(Collectors.toSet());
    }

    public String getExtendsContent() {
        return this.superClass == null
                ? ApiConstants.EMPTY
                : " extends {EXTENDS}".replace("{EXTENDS}", this.superClass.getSimpleName());
    }

    public String getDeclaredFieldsContent() {
        StringBuilder declaredFields = new StringBuilder(ApiConstants.EMPTY);
        for (VariableNode variable : this.getVariables()) {
            declaredFields.append(
                    "\tprivate {FIELD_TYPE} {FIELD_NAME};\n"
                            .replace("{FIELD_TYPE}", variable.getView())
                            .replace("{FIELD_NAME}", variable.getName())
            );
        }
        return declaredFields.toString();
    }

    public String getEnumFieldContent() {
        return Arrays.stream(this.clazz.getDeclaredFields())
                .filter(field -> field.isEnumConstant())
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }
}