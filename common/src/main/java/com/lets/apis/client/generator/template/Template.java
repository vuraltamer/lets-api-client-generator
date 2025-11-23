package com.lets.apis.client.generator.template;

import com.lets.apis.client.generator.writer.util.FileUtil;
import lombok.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Template {

    private Set<Model> models;

    @Getter
    @Setter
    @EqualsAndHashCode(exclude = "name")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Model {
        private String name;
        private String content;
    }

    public static Template collect() {
        Set<Model> collect = templateNames.stream()
                .map(templateName -> new Model(templateName, fetchContent(templateName)))
                .collect(Collectors.toSet());
        return new Template(collect);
    }

    private static String fetchContent(String templateName) {
        return FileUtil.read("/template/", templateName);
    }

    public static final List<String> templateNames = Arrays.asList("api-enum", "api-model", "build-gradle", "feign-client", "settings-gradle");
}