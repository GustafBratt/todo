package archtest;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * ArchUnit test to verify domain model immutability.
 *
 * Based on the architectural decision that the domain model should be immutable.
 */
class DomainImmutabilityArchTest {

    private static JavaClasses domainClasses;

    @BeforeAll
    static void importClasses() {
        domainClasses = new ClassFileImporter()
                .importPackages("nu.forsenad.todo.domain");
    }

    @Test
    void allInstanceFieldsShouldBeFinal() {
        ArchRule rule = fields()
                .that().areDeclaredInClassesThat().resideInAPackage("..domain..")
                .and().areNotStatic()
                .should().beFinal()
                .because("all domain objects should be immutable with final fields");

        rule.check(domainClasses);
    }

    @Test
    void domainClassesShouldBeFinal() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain..")
                .and().areNotInterfaces()
                .and().areNotEnums()
                .should().haveModifier(JavaModifier.FINAL)
                .because("domain classes should be final to ensure complete immutability");

        rule.check(domainClasses);
    }

    @Test
    void domainClassesShouldNotHaveSetters() {
        ArchRule noSettersRule = methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..domain..")
                .and().areNotStatic()
                .and().arePublic()
                .should().haveNameNotMatching("set[A-Z].*")
                .because("immutable objects should not have setter methods");

        noSettersRule.check(domainClasses);
    }
}