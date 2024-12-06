package com.lets.apis.api.caller.constants;

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

    public static String FEIGN_CLASS_CONTENT = """
            package {PACKAGE};
            
            {IMPORTS}
            
            @FeignClient(url=\"${{FEIGN_URL}}\", {PATH})
            public interface {CLASS_NAME} {
            {METHODS}
            }
            """;

    public static String API_CLASS_CONTENT = """
            package {PACKAGE};
            
            {IMPORTS}
            
            @Getter
            @Setter
            @NoArgsConstructor
            public class {CLASS_NAME}{TYPE_PARAMETERS}{EXTENDS} {
            {VARIABLES}
            }
            """;

    public static String API_ENUM_CONTENT = """
            package {PACKAGE};
            
            import lombok.AccessLevel;
            import lombok.AllArgsConstructor;
            import lombok.Getter;

            @Getter
            @AllArgsConstructor(access = AccessLevel.PRIVATE)
            public enum {ENUM_NAME} {
            {VARIABLES}
            }
            """;

    public static String PARAMETERIZED = "{RAW}<{CONTENT}>";

    public static final String BASE_PATH = "com.lets.caller.clients.";
}