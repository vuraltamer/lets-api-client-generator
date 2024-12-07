package com.lets.apis.client.generator.model;

import com.lets.apis.client.generator.creator.ControllerPathCreator;
import com.lets.apis.client.generator.constants.ApiConstants;
import com.lets.apis.client.generator.creator.ControllerClassesCreator;
import com.lets.apis.client.generator.loader.ControllerContentLoader;
import com.lets.apis.client.generator.creator.ControllerMethodCreator;
import com.lets.apis.client.generator.util.base.Util;
import com.lets.apis.client.generator.constants.CallerConstants;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ControllerDetail extends ControllerContentLoader {
    private Class clazz;
    private String className;
    private String packageName;
    private String fullName;
    private String[] path;
    private List<ControllerMethodDetail> methods;
    private Set<Class> classes;
    private String content;

    public ControllerDetail(Class clazz) {
        this.clazz = clazz;
        this.className = Util.getControllerName(clazz);
        this.packageName = Util.getPackageName(clazz);
        this.fullName = Util.getControllerFullName(clazz);
    }

    public static ControllerDetail create(Class clazz) {
        ControllerDetail controllerDetail = new ControllerDetail(clazz);
        controllerDetail.setPath(ControllerPathCreator.create(controllerDetail));
        controllerDetail.setMethods(ControllerMethodCreator.create(controllerDetail));
        controllerDetail.setClasses(ControllerClassesCreator.create(controllerDetail));
        controllerDetail.setContent(ControllerContentLoader.create(controllerDetail));
        return controllerDetail;
    }

    public String getImports() {
        Set<ImportDetail> imports = getMethodImports();
        imports.addAll(ImportDetail.create(this.classes));
        imports.addAll(ImportDetail.create(CallerConstants.CONTROLLER_IMPORTS));
        return imports.stream()
                .filter(importDetail -> !Util.isPrimitive(importDetail.getClazz()))
                .sorted(Comparator.comparing(ImportDetail::getFullName))
                .map(imp -> "import {IMPORT};\n".replace("{IMPORT}", imp.getFullName()))
                .collect(Collectors.joining());
    }

    private Set<ImportDetail> getMethodImports() {
        return this.methods.stream()
                .flatMap(method -> method.getImports().stream())
                .collect(Collectors.toSet());
    }

    public String getPathView() {
        if (this.path == null || this.path.length == 0) {
            return ApiConstants.EMPTY;
        } else if (this.path.length == 1) {
            return "path = \"{PATH}\""
                    .replace("{PATH}", this.path[0]);
        }
        return "path = {\"{PATH}\"}"
                .replace("{PATH}",
                        Arrays.stream(this.path)
                            .collect(Collectors.joining(", "))
        );
    }
}