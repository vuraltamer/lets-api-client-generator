package com.lets.apis.client.generator.properties.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lets.apis.client.generator.mapper.Mapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallerProperties {

    private String clientName;
    private String clientPath;
    private String clientVersion;
    private String groupId;
    private String javaVersion;
    private String clientDependencies;

    public static CallerProperties create(String json) {
        return Mapper.toObject(json, CallerProperties.class);
    }
}