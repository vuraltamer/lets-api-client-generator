package com.lets.apis.client.generator.constants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Set;

public class CallerConstants {

    public static Set<Class> MODEL_IMPORTS = Set.of(
            Getter.class, Setter.class, NoArgsConstructor.class
    );

    public static Set<Class> CONTROLLER_IMPORTS = Set.of(FeignClient.class);

    public static String PARAMETERIZED = "{RAW}<{CONTENT}>";

    public static final String BASE_PATH = "com.lets.apis.client.generator.";

    public static final String CONTROLLER = ".controller";

    public static final String CONTROLLERS = ".controllers";

    public static final String FEIGN = ".feign";
}