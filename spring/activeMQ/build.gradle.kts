plugins {
	java
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "8.0.0"
}

group = "merboxel.example"
version = "0.0.1-SNAPSHOT"
description = "Example project for activeMQ"

val xmlBind = "4.0.4"
val jaxB = "4.0.6"
val httpclient5 = "5.5.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-activemq")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:$xmlBind")
    implementation("org.glassfish.jaxb:jaxb-runtime:$jaxB")
    implementation("org.apache.httpcomponents.client5:httpclient5:$httpclient5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:testcontainers-activemq")
	testImplementation("org.testcontainers:testcontainers-junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}