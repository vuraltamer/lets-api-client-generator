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
                mavenBom "org.springframework.cloud:spring-cloud-dependencies:2021.0.8"
            }
        }
        
        dependencies {
            implementation 'org.springframework.boot:spring-boot-starter-actuator'
            implementation 'org.springframework.boot:spring-boot-starter-web'
            implementation 'org.springframework.cloud:spring-cloud-starter-config'
            implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
            implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
            {IMPLEMENTATIONS}
            implementation 'org.projectlombok:lombok:1.18.24'
            annotationProcessor 'org.projectlombok:lombok:1.18.24'
            compileOnly 'org.projectlombok:lombok:1.18.28'
            testImplementation("org.springframework.boot:spring-boot-starter-test")
        }
        
        tasks.named('test') {
            useJUnitPlatform()
        }
        """;

    public static final String SETTINGS_GRADLE_CONTENT = """
            rootProject.name = '{API_NAME}'
            """;
}
