package com.lets.apis.client.generator.model;

import com.lets.apis.client.generator.util.base.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ImportDetail {
    private String packageName;
    private String className;
    private String fullName;
    private Class clazz;

    public ImportDetail(Class clazz) {
        this.clazz  = clazz;
        this.className = clazz.getSimpleName();
        this.packageName = Util.getPackageName(clazz);
        this.fullName = Util.getFullName(clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportDetail detail = (ImportDetail) o;
        return Objects.equals(clazz, detail.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clazz);
    }

    public static Set<ImportDetail> create(Set<Class> classes) {
        return classes.stream()
                .map(clazz -> new ImportDetail(clazz))
                .collect(Collectors.toSet());
    }
}