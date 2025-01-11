plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.eye-on"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
//    maven { url = uri("https://repo.spring.io/milestone") }
//    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Spring AI
//    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0-SNAPSHOT"))
//    implementation ("org.springframework.ai:spring-ai-anthropic-spring-boot-starter")
//    implementation("org.springframework.ai:spring-ai-anthropic")

    implementation("commons-io:commons-io:2.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20240205")
    implementation("com.squareup.okio:okio:3.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")


    // Jsoup
    implementation ("org.jsoup:jsoup:1.15.4")

    // webclient and json
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:3.0.0")
    implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // netty 오류 해결
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.116.Final:osx-aarch_64")
    implementation("io.netty:netty-transport-native-epoll:4.1.116.Final")

    // Env
    implementation("me.paulschwarz:spring-dotenv:4.0.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
