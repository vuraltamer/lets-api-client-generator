# 🚀 lets-api-client-generator

**Feign Client API Generator for Spring Boot Microservices**

`lets-api-client-generator` is a Gradle plugin that simplifies the generation of Feign Client classes from your Spring Boot service implementations. It scans your annotated service classes and automatically generates Feign-compatible clients, optionally packaged as a JAR file for reuse across multiple projects.

---

## 📦 Installation

Add the following to your project's `build.gradle`:

```groovy
plugins {
    id 'io.github.lets.apis.client.generator' version '1.0.0'
}

dependencies {
    implementation 'io.github.lets.apis.client.generator:common:1.0.0'
}
```
Add the following to your project's `settings.gradle`:

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://maven.pkg.github.com/vuraltamer/lets-api-client-generator")
            credentials {
                username = project.findProperty("gpr.user")
                password = project.findProperty("gpr.token")
            }
        }
    }
}
```

> Alternatively, you can download the file and apply it locally:

```groovy
apply from: 'lets-client-generator/plugin/src/main/resources/generator/lets-generate-api-client.gradle'
apply from: 'lets-client-generator/plugin/src/main/resources/generator/lets-publish-api-client.gradle'
```

## ⚙️ Lets try

```properties
gradle generateApiClient \
    -PclientName=user-feign-client \
    -PclientPath= \
    -PclientVersion=1.0.0.0 \
    -PjavaVersion=17 \
    -PclientDependencies=com.sample.repo:1.0.0,com.sample.repo:1.0.1

```
```properties
gradle publishApiClient \
        -PclientName=user-feign-client \
        -PgroupId=com.lets.apis.client \
        -PartifactId=user-feign-client \
        -Pversion=1.0.0.0 \
        -Prepository=http://xxx.xxx.x.x:xxxx/repository/maven-releases/ \
        -Puser=admin  \
        -Ppassword=pass  \
        -PallowInsecure=true


```
---

## 📸 Screenshots


### 1. Sample service classes

![services](img/img_1.png)

---

### 2. Running the Gradle task

```bash
gradle generateApiClient
```

![generate](img/img_2.png)

---

### 3. Generated client output

![output](img/img_3.png)

---

## 🔍 Example Project


## 📦 Bean definition for client jar usage

```groovy
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    implementation 'io.github.openfeign:feign-okhttp' // For patchMapping usage
    implementation 'generated-fein-client'
```

```java

    @EnableFeignClients // For your configuration
    
    @Bean // For patchMapping usage
    public Client feignClient() {
        return new OkHttpClient(new okhttp3.OkHttpClient());
    }

    @Bean // For Multipart file usage
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters()));
    }

    @Bean("feignHttpMessageConverters")
    public ObjectFactory<HttpMessageConverters> messageConverters() {
        return () -> new HttpMessageConverters();
    }
```

---

You can try out the generator with the example project below:

👉 [Sample Repository – feign-client-generator-test](https://github.com/vuraltamer/feign-client-generator-test)

---

## 💡 Notes

- This tool is useful in microservice architectures where Feign clients are preferred for inter-service communication.
- Generated clients can be reused across modules or even published to internal Maven/Nexus repositories.
- Customize the behavior by editing the `caller-config.properties` file.

---

## 🧑‍💻 Contributing

Feel free to open issues or submit pull requests. Contributions are welcome!

---