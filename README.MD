**FEIGN CLIENT API GENERATOR**

**lets-api-client-generator jar implementation**

    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/vuraltamer/lets-api-client-generator")
            credentials {
                username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }

    dependencies {
        implementation 'com.lets.apis.client.generator:lets-api-client-generator:1.0.0'
    }

    apply from: 'https://raw.githubusercontent.com/vuraltamer/lets-api-client-generator/refs/heads/master/doc/generate-client.gradle'

**create caller-config.properties file for service class**

    com.lets.apis.client.generator.java-version= 17 // OPTIONAL : set client jara version (default= 17)
    com.lets.apis.client.generator.gradle-version= 8.5 // OPTIONAL : set gradle version for gradle wrapper (default=8.5)
    com.lets.apis.client.generator.api-name= person-service-client // REQUIRED :
    com.lets.apis.client.generator.api-client-path=/temp // OPTIONAL : default:your project directory
    com.lets.apis.client.generator.scan-package= com.project.main // OPTIONAL : must be last package before the subpackages (if not specified, it scans all classes from the base package)
    com.lets.apis.client.generator.dependencies= [org.springframework.boot:spring-boot-starter-data-mongodb,\
            \ sample:dependencies:1.0.0] // MAYBE REQUIRED : third party your dependencies for generating

caller-config path : src/main/resources/caller-config.properties

**generate feign client classes**

run this task with : apply from: 'https://raw.githubusercontent.com/vuraltamer/lets-api-client-generator/refs/heads/master/doc/generate-client.gradle'

    gradle generateApiClientJar

alternatively use this gradle codes from local generate-client.gradle : apply from: 'generate-client.gradle'

    https://raw.githubusercontent.com/vuraltamer/lets-api-client-generator/refs/heads/master/doc/generate-client.gradle

**lets try**

- caller-config

![img.png](img/img.png)

- services

![img_1.png](img/img_1.png)

- run gradle generateApiClientJar task
 
![img_2.png](img/img_2.png)

- finally

![img_3.png](img/img_3.png)


**sample repo** : https://github.com/vuraltamer/feign-client-generator-test