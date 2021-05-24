plugins {
	// Plugin versions are left out because other projects use this as a library, and gradle won't allow the library to set plugin versions.
	// This restriction means this project can't function on its own, only when used by another project.
	// This file includes some Spring dependencies because we have a bit of Spring util code in this project.
	// Information on how to get Spring working in a multi-project build: stackoverflow.com/a/54136970

	id("org.jetbrains.kotlin.jvm")
	id("io.spring.dependency-management")
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
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

	//spring is used in some util code
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.1")

	testImplementation("junit:junit:4.12")
}