package com.github.alexandrenavarro.springboot3sample;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;


public class ArchUnitTest {

    private static final String WEB = "Web";
    private static final String SERVICE = "Service";
    private static final String PERSISTENCE = "Persistence";
    private final JavaClasses classes = new ClassFileImporter().importPackages("com.github.alexandrenavarro.springboot3sample");

    @Test
    void shouldHaveCorrectNamingConvention() {
        ArchRuleDefinition.classes()
                .that().areAnnotatedWith(SpringBootApplication.class).should().haveSimpleNameEndingWith("Application")
                .check(classes);
        ArchRuleDefinition.classes()
                .that().areAnnotatedWith(Configuration.class).should().haveSimpleNameEndingWith("Config")
                .check(classes);
        ArchRuleDefinition.classes()
                .that().areAnnotatedWith(RestController.class).should().haveSimpleNameEndingWith("RestController")
                .check(classes);
        ArchRuleDefinition.classes()
                .that().areAnnotatedWith(Entity.class).should().haveSimpleNameStartingWith("Jpa")
                .check(classes);
    }

    @Test
    void shouldNotUseSomeDependencies() {
        ArchRuleDefinition.noClasses().should()
                .dependOnClassesThat().resideInAPackage("com.google.common.*")
                .check(classes);

        // TODO exclude the test of the noClasses
//        ArchRuleDefinition.noClasses().should()
//                .beAnnotatedWith(Data.class)
//                .check(classes);
    }

    @Test
    void shouldRespectLayers() {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer(WEB).definedBy("..web..")
                .layer(SERVICE).definedBy("..service..")
                .layer(PERSISTENCE).definedBy("..persistence..")
                .whereLayer(WEB).mayNotBeAccessedByAnyLayer()
                .whereLayer(SERVICE).mayOnlyBeAccessedByLayers(WEB)
                .whereLayer(PERSISTENCE).mayOnlyBeAccessedByLayers(SERVICE)
                .check(classes);
    }

}
