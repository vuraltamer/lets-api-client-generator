package com.lets.apis.client.generator.model;

import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.loader.ApiClientModelContentLoader;
import com.lets.apis.client.generator.model.node.SuperClassNode;
import com.lets.apis.client.generator.model.node.TypeParametersNode;
import com.lets.apis.client.generator.model.node.VariableNode;
import com.lets.apis.client.generator.util.base.Util;
import com.lets.apis.client.generator.constants.CallerConstants;
import lombok.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.lets.apis.client.generator.util.base.Util.isStatic;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiClientModelDetail {
    private Class clazz;
    private SuperClassNode superClass;
    private TypeParametersNode typeParameters;
    private List<VariableNode> variables;
    private String packageName;
    private String className;
    private String fullName;
    private String content;

    public ApiClientModelDetail(Class clazz) {
        this.clazz  = clazz;
        this.className = clazz.getSimpleName();
        this.packageName = Util.getPackageName(clazz);
        this.fullName = Util.getFullName(clazz);
    }

    public static ApiClientModelDetail create(Class clazz) {
        ApiClientModelDetail apiClientModelDetail = new ApiClientModelDetail(clazz);
        apiClientModelDetail.setTypeParameters(TypeParametersNode.create(clazz));
        apiClientModelDetail.setSuperClass(SuperClassNode.create(clazz));
        apiClientModelDetail.setVariables(createVariables(clazz));
        apiClientModelDetail.setContent(ApiClientModelContentLoader.load(apiClientModelDetail));
        return apiClientModelDetail;
    }

    private static List<VariableNode> createVariables(Class clazz) {
        List<VariableNode> variables = new ArrayList<>();
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (Util.isStatic(declaredField)) {
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
        ApiClientModelDetail that = (ApiClientModelDetail) o;
        return Objects.equals(className, that.className) && Objects.equals(packageName, that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, packageName);
    }

    public String getImportsContent() {
        Set<ImportDetail> imports = ImportDetail.create(CallerConstants.MODEL_IMPORTS);
        if (this.getSuperClass() != null && this.getSuperClass().getImports() != null) {
            imports.addAll(this.getSuperClass().getImports());
        }
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