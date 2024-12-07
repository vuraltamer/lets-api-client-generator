package com.lets.apis.client.generator.model;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDetail {
    String directoryPath;
    Set<Class> controllerClass;
    List<ControllerDetail> controllerDetails;
    Set<ControllerModelDetail> modelDetails;
    GradleDetail gradleDetail;
}