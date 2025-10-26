plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "8.0.0"
}

group = "merboxel.example"
version = "0.0.1-SNAPSHOT"
description = "Example project for activeMQ"

val jacksonVersion = "3.0.1"
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
    implementation("tools.jackson.core:jackson-databind:$jacksonVersion")
    implementation("tools.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:3.0-rc5")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:$xmlBind")
    implementation("org.glassfish.jaxb:jaxb-runtime:$jaxB")
    implementation("org.apache.httpcomponents.client5:httpclient5:$httpclient5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:activemq")
	testImplementation("org.testcontainers:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}