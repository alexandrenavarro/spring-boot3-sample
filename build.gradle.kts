plugins {
	java
}

group = "com.github.alexandrenavarro"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}


dependencies {
	implementation(platform("org.springframework.boot:spring-boot-dependencies:3.1.5"))
	implementation("org.springframework.boot:spring-boot-starter")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

