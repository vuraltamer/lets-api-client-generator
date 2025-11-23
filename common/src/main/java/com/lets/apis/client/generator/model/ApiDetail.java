package com.lets.apis.client.generator.model;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ApiDetail {
    String directoryPath;
    Set<Class> controllerClass;
    List<ApiClientDetail> apiClientDetails;
    Set<ApiClientModelDetail> modelDetails;
    GradleDetail gradleDetail;
}