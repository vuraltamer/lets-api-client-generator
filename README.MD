**FEIGN CLIENT API GENERATOR**

**create caller-config.properties file**

    com.lets.apis.api.caller.java-version= 17 //set your api version
    com.lets.apis.api.caller.gradle-version= 8.5 //set your api version
    com.lets.apis.api.caller.api-name= person-service-client
    com.lets.apis.api.caller.scan-package= com.project.main // must be last package before the subpackages (if not specified, it scans all classes from the base package)
    com.lets.apis.api.caller.dependencies= [org.springframework.boot:spring-boot-starter-data-mongodb,\
        \ org.springframework.boot:spring-boot-starter-data-test] // third party dependencies

caller-config path : src/resources/caller-config.properties

**create feign client classes**

add this task to build.gradle

    task createFeignClient {
        doLast {
            def mainClass = 'com.lets.apis.api.caller.ApiCallerGenerator'
            def args = []
            javaexec {
                main = mainClass
                args = args
                classpath = sourceSets.main.runtimeClasspath
            }
        }
    }

**run createFeignClient**

createFeignClient task is executed, a .generator file is created in the project root
