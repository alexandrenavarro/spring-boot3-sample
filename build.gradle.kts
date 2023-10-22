plugins {
	java
	checkstyle
	id("org.openrewrite.rewrite") version "6.4.0"
	id("org.sonarqube") version "4.4.1.3373"
	id("com.github.ben-manes.versions") version "0.49.0"
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

rewrite {

//activeRecipe("org.openrewrite.java.format.AutoFormat")
activeRecipe("org.openrewrite.java.format.BlankLines")
activeRecipe("org.openrewrite.java.format.EmptyNewlineAtEndOfFile")
//activeRecipe("org.openrewrite.java.format.MethodParamPad")
//activeRecipe("org.openrewrite.java.format.NormalizeFormat")
//activeRecipe("org.openrewrite.java.format.NormalizeLineBreaks")
activeRecipe("org.openrewrite.java.format.NormalizeTabsOrSpaces")
activeRecipe("org.openrewrite.java.format.NoWhitespaceAfter")
activeRecipe("org.openrewrite.java.format.NoWhitespaceBefore")
activeRecipe("org.openrewrite.java.format.OperatorWrap")
activeRecipe("org.openrewrite.java.format.PadEmptyForLoopComponents")
activeRecipe("org.openrewrite.java.format.RemoveTrailingWhitespace")
activeRecipe("org.openrewrite.java.format.SingleLineComments")
activeRecipe("org.openrewrite.java.format.Spaces")
//activeRecipe("org.openrewrite.java.format.TabsAndIndents")
activeRecipe("org.openrewrite.java.format.TypecastParenPad")
//activeRecipe("org.openrewrite.java.format.WrappingAndBraces")
}


