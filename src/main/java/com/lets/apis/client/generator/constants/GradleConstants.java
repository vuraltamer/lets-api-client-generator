package com.lets.apis.client.generator.constants;

public class GradleConstants {

    public static String BUILD_GRADLE_CONTENT =
        """
        plugins {
            id 'java'
            id 'org.springframework.boot' version '3.4.0'
            id 'io.spring.dependency-management' version '1.1.6'
        }
        
        group = 'com.lets.apis.client.generator'
        version = '1.0.0'
        
        java {
            toolchain {
                languageVersion = JavaLanguageVersion.of({JAVA_VERSION})
            }
        }
        
        jar {
            archiveBaseName = '{JAR_NAME}'
            archiveVersion = '1.0.0'
        }
        
        configurations {
            compileOnly {
                extendsFrom annotationProcessor
            }
        }
        
        repositories {
            maven { url 'https://repo.spring.io/milestone' }
            maven { url 'https://repo.spring.io/snapshot' }
            gradlePluginPortal()
            mavenCentral()
        }
        
        dependencyManagement {
            imports {
                mavenBom "org.springframework.cloud:spring-cloud-dependencies:2022.0.5"
            }
        }
        
        dependencies {
            implementation 'org.springframework.boot:spring-boot-starter-actuator'
            implementation 'org.springframework.boot:spring-boot-starter-web'
            implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
            {IMPLEMENTATIONS}
            implementation 'org.projectlombok:lombok:1.18.24'
            annotationProcessor 'org.projectlombok:lombok:1.18.24'
            compileOnly 'org.projectlombok:lombok:1.18.28'
            testImplementation("org.springframework.boot:spring-boot-starter-test")
        }
        
        tasks.register('create-wrapper', Wrapper) {
            gradleVersion = '{GRADLE_VERSION}'
            distributionType = Wrapper.DistributionType.ALL
        }
        """;

    public static final String SETTINGS_GRADLE_CONTENT = """
            rootProject.name = '{API_NAME}'
            """;

    public static final String GENERATE_CLIENT_JAR = """
            tasks.register('generateApiClientJar') {
                dependsOn tasks.named('createApiClientClasses')
                doLast {
                    exec {
                        workingDir = file("${apiClientPath}/.generator/${apiName}")
                        if (System.getProperty('os.name').toLowerCase().contains('windows')) {
                            commandLine 'cmd', '/c', 'gradle create-wrapper'
                        } else {
                            commandLine 'bash', '-c', 'gradle create-wrapper'
                        }
                    }
                }
                doLast {
                    exec {
                        workingDir = file("${apiClientPath}/.generator/${apiName}")
                        if (System.getProperty('os.name').toLowerCase().contains('windows')) {
                            commandLine 'cmd', '/c', './gradlew jar'
                        } else {
                            commandLine 'bash', '-c', './gradlew jar'
                        }
                    }
                }
            }
            
            tasks.register('createApiClientClasses', JavaExec) {
                mainClass = 'com.lets.apis.client.generator.ApiClientGenerator'
                classpath = sourceSets.main.runtimeClasspath
                args = []
            }
            
            def loadProperties() {
                def props = new Properties()
                def propertiesFile = file('src/main/resources/caller-config.properties')
                if (!propertiesFile.exists()) {
                    throw new GradleException("caller-config.properties file not found in src/main/resources")
                }
                propertiesFile.withInputStream { stream ->
                    props.load(stream)
                }
                return props
            }
            
            def getApiName() {
                def callerConfig = loadProperties()
                def apiName = callerConfig.getProperty('com.lets.apis.client.generator.api-name')
                if (!apiName) {
                    throw new GradleException("api name not specified in caller-config.properties")
                }
                return apiName
            }
            
            def getApiClientPath() {
                def callerConfig = loadProperties()
                def apiClientPath = callerConfig.getProperty('com.lets.apis.client.generator.api-client-path')
                if (!apiClientPath || apiClientPath.trim().isEmpty()) {
                    return projectDir.absolutePath
                }
                return apiClientPath
            }
            """;
}