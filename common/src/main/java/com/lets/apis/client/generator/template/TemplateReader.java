package com.lets.apis.client.generator.template;

import static com.lets.apis.client.generator.constants.ApiConstants.EMPTY;

public class TemplateReader {

    private static Template template;

    public static Template template() {
        if (template == null) {
            return template = read();
        }
        return template;
    }

    private static Template read() {
        return Template.collect();
    }

    public static String content(String fileName) {
        return template().getModels().stream()
                .filter(model -> model.getName().equals(fileName))
                .map(model -> model.getContent())
                .findAny()
                .orElse(EMPTY);
    }
}