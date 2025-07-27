package io.github.yupd.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("io.github.yupd");

    @Test
    void layeredArchitectureShouldBeRespected() {
        ArchRule rule = layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer("Domain-Service").definedBy("io.github.yupd.domain.service..")
                .layer("Model").definedBy("io.github.yupd.domain.model..")
                .layer("Ports").definedBy("io.github.yupd.domain.ports..")
                .layer("Application").definedBy("io.github.yupd.application..")
                .layer("Infrastructure").definedBy("io.github.yupd.infrastructure..")
                .layer("Utils").definedBy("io.github.yupd.infrastructure.utils..")
                .whereLayer("Domain-Service").mayOnlyAccessLayers("Model", "Ports", "Utils")
                .whereLayer("Model").mayOnlyAccessLayers("Utils")
                .whereLayer("Ports").mayOnlyAccessLayers("Model")
                .whereLayer("Application").mayOnlyAccessLayers("Model", "Ports", "Utils")
                .whereLayer("Infrastructure").mayOnlyAccessLayers("Model", "Ports", "Utils")
                .whereLayer("Utils").mayOnlyAccessLayers("Utils");

        rule.check(classes);
    }

    @Test
    void portsShouldBeInterfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("io.github.yupd.domain.ports..")
                .should().beInterfaces();

        rule.check(classes);
    }
}