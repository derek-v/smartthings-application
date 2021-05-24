plugins {
	id("org.jetbrains.kotlin.jvm") version "1.5.0"

	//This project doesn't really use Spring, but common does so we're stuck including it.
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.jetbrains.kotlin.plugin.spring") version "1.5.0"
	id("org.springframework.boot") version "2.5.0"
}

group = "com.example.smartthings"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation(project(":common"))

	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	testImplementation("junit:junit:4.12")
}
