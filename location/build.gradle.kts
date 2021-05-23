import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	//plugin versions are specified in common

	id("org.jetbrains.kotlin.jvm") version "1.5.0"

	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.jetbrains.kotlin.plugin.spring") version "1.5.0"
	id("org.springframework.boot") version "2.5.0"
}

group = "com.example"
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
}

springBoot {
	mainClassName = "com.example.smartthings.location.LocationSpringAppKt"
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
