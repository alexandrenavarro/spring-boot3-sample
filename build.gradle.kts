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
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.0"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    // TODO fix problem with version
    compileOnly("org.projectlombok:lombok:1.18.30")
    compileOnly("org.jspecify:jspecify:0.3.0")

    runtimeOnly("com.h2database:h2")

    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.tngtech.archunit:archunit:1.2.0")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testRuntimeOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


tasks.register("checkstyleSpecificProject", Checkstyle::class) {
    configFile = File("config/checkstyle/checkstyle-specific-project.xml")
    source = fileTree("$rootDir/src/main/java")
    classpath = files()
}

tasks {
    check {
        dependsOn("checkstyleSpecificProject")
    }
}

// TODO create recipe.yaml
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

