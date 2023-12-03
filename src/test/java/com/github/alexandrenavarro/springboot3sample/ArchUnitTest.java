package com.github.alexandrenavarro.springboot3sample;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.DependencyRules;
import com.tngtech.archunit.library.GeneralCodingRules;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;


class ArchUnitTest {

    private static final String ROOT_PACKAGE = "com.github.alexandrenavarro.springboot3sample";

    private static final String CONFIG = "Config";
    private static final String CONFIG_PACKAGE = ROOT_PACKAGE + "." + "config..";

    private static final String PERSISTENCE = "Persistence";
    public static final String PERSISTENCE_PACKAGE = ROOT_PACKAGE + "." + "persistence..";

    public static final String PERSISTENCE_MODEL_PACKAGE = ROOT_PACKAGE + "." + "persistence.model..";

    private static final String SERVICE = "Service";
    public static final String SERVICE_PACKAGE = ROOT_PACKAGE + "." + "service..";

    private static final String WEB = "Web";
    public static final String WEB_PACKAGE = ROOT_PACKAGE + "." + "web..";


    private final JavaClasses classes = new ClassFileImporter()
            //.withImportOption(new ImportOption.DoNotIncludeTests()) // Can be used if needed
            .importPackages(ROOT_PACKAGE);


    @Test
    void noClassesShouldNotThrowGenericException() {
        GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.check(classes);
    }

    @Test
    void noClassesShouldNotAccesssStandardStream() {
        GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.check(classes);
    }

    @Test
    void noClassesShouldNotUseFieldInjection() {
        GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classes);
    }

    @Test
    void noClassesShouldDependUpperPackages() {
        DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES.check(classes);
    }

    @Test
    void noPackageShouldBeFreeOfCycle() {
        slices().matching(ROOT_PACKAGE + ".(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }

    @Test
    void classesWithOnlyStaticMethodsShouldEndingWithS() {
        final DescribedPredicate<? super JavaClass> classesWithOnlyStaticMethods = new DescribedPredicate<>("Class with only Static methods") {
            public boolean test(final JavaClass javaClass) {
                return !javaClass.getMethods().isEmpty()
                        && javaClass.getMethods()
                        .stream()
                        .allMatch(e -> e.getModifiers().contains(JavaModifier.STATIC) && !e.getName().equals("main"));
            }
        };
        classes().that(classesWithOnlyStaticMethods)
                .should().haveSimpleNameEndingWith("s") // or Utils
                .andShould().haveModifier(JavaModifier.FINAL)
                .check(classes);
    }

    @Test
    void noClassesShouldUseSomeDependencies() {
        // Can be done through checkstyle
        noClasses().should()
                .dependOnClassesThat().resideInAnyPackage(
                        // No Logger implementation
                        "com.google.common.flogger..",
                        "org.jboss.logging..",
                        "java.util.logging..",
                        "org.apache.log4j..",
                        "org.apache.logging.log4j..",
                        "ch.qos.logback..",
                        // No Jodatime
                        "org.joda.time..")
                .because("Do not use any logger implementation except through Slf4j with notably lombok @Slf4j")
                .check(classes);

        noClasses().should()
                .dependOnClassesThat().resideInAnyPackage(
                        // No guava
                        "com.google.common..")
                .because("Use commons-lang or commons-collections instead")
                .check(classes);

        GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME.check(classes);

        // TODO Not possible because lombok annotation dispears at runtime, see how to do it through equivalent of https://github.com/skuzzle/restrict-imports-enforcer-rule before test
//        ArchRuleDefinition.noClasses().should()
//                .beAnnotatedWith(Data.class)
//                .check(classes);

    }

    @Test
    void classesShouldHaveCorrectApplicationNamingConvention() {
        classes().that().areAnnotatedWith(SpringBootApplication.class)
                .should().haveSimpleNameEndingWith("Application")
                .andShould()
                .resideInAPackage(ROOT_PACKAGE)
                .check(classes);
        noClasses().that().resideOutsideOfPackage(ROOT_PACKAGE)
                .should().beAnnotatedWith(SpringBootApplication.class)
                .check(classes);
    }

    @Test
    void classesShouldRespectServiceNamingRules() {
        classes().that().areAnnotatedWith(Service.class)
                .should().haveSimpleNameEndingWith(Service.class.getSimpleName())
                .andShould().resideInAPackage(SERVICE_PACKAGE)
                .check(classes);
        noClasses().that().resideOutsideOfPackage(SERVICE_PACKAGE)
                .should().beAnnotatedWith(Service.class)
                .check(classes);
    }

    @Test
    void classesShouldRespectConfigNamingRules() {
        classes().that().areAnnotatedWith(Configuration.class)
                .should().haveSimpleNameEndingWith("Config")
                .andShould().resideInAPackage(CONFIG_PACKAGE)
                .check(classes);
    }

    @Test
    void classesAndMethodsShouldRespectPersistenceNamingRules() {
        classes().that().areAnnotatedWith(Entity.class)
                .should().haveSimpleNameStartingWith("Jpa")
                .andShould().resideInAPackage(PERSISTENCE_MODEL_PACKAGE)
                .check(classes);
        noClasses().that().resideOutsideOfPackage(PERSISTENCE_MODEL_PACKAGE)
                .should().beAnnotatedWith(Entity.class);
        classes().that().areAnnotatedWith(Repository.class)
                .should().haveSimpleNameEndingWith("Repository")
                .andShould().resideInAPackage(PERSISTENCE_PACKAGE)
                .check(classes);
        noClasses().that().resideOutsideOfPackage(PERSISTENCE_PACKAGE)
                .should().beAnnotatedWith(Repository.class);
        noClasses().that().resideOutsideOfPackage(PERSISTENCE_PACKAGE)
                .should().accessClassesThat().areAssignableTo(EntityManager.class)
                .check(classes);
        noMethods().that().areDeclaredInClassesThat().haveNameMatching(".*Repository")
                .should().declareThrowableOfType(SQLException.class)
                .check(classes);
    }

    @Test
    void classesShouldRespectWebNamingRules() {
        classes().that().areAnnotatedWith(RestController.class)
                .should().haveSimpleNameEndingWith(RestController.class.getSimpleName())
                .andShould().resideInAPackage(WEB_PACKAGE)
                .check(classes);
        noClasses().that().resideOutsideOfPackage(WEB_PACKAGE)
                .should().beAnnotatedWith(RestController.class)
                .check(classes);
    }

    @Test
    void classesShouldRespectLayers() {
        // TODO see how to do it through equivalent of https://github.com/skuzzle/restrict-imports-enforcer-rule or macker before test
        layeredArchitecture()
                .consideringAllDependencies()
                .layer(CONFIG).definedBy(CONFIG_PACKAGE)
                .layer(PERSISTENCE).definedBy(PERSISTENCE_PACKAGE)
                .layer(SERVICE).definedBy(SERVICE_PACKAGE)
                .layer(WEB).definedBy(WEB_PACKAGE)
                .whereLayer(CONFIG).mayNotBeAccessedByAnyLayer()
                .whereLayer(WEB).mayNotBeAccessedByAnyLayer()
                .whereLayer(SERVICE).mayOnlyBeAccessedByLayers(WEB)
                .whereLayer(PERSISTENCE).mayOnlyBeAccessedByLayers(SERVICE)
                .check(classes);
    }

}
