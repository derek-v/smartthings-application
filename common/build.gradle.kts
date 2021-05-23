plugins {
	//Plugin versions are left out because other projects use this as a library, and gradle won't allow the library to set plugin versions.
	//This restriction means this project can't function on its own, only when used by another project.

	id("org.jetbrains.kotlin.jvm")

	//Note: Spring is only used in some util code; the actual APIs are in other projects.
	id("io.spring.dependency-management")
	id("org.jetbrains.kotlin.plugin.spring")
	id("org.springframework.boot")
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

	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.1")

	testImplementation("junit:junit:4.12")
}