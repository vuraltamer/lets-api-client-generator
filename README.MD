**FEIGN CLIENT API GENERATOR**

**create caller-config.properties file**

    com.lets.api.caller.java-version= 17 //set your api version
    com.lets.api.caller.gradle-version= 8.5 //set your api version
    com.lets.api.caller.api-name= person-service-client
    com.lets.api.caller.dependencies= [org.springframework.boot:spring-boot-starter-data-mongodb,\
        \ org.springframework.boot:spring-boot-starter-data-test] // third party dependencies

caller-config path : src/resources/caller-config.properties

**create feign client classes**

add this task to build.gradle

    task createFeignClient {
        doLast {
            def mainClass = 'com.lets.api.caller.ApiCallerGenerator'
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
