package com.jexunit.examples.arithmeticaltests;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.JExUnitConfig;
import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.model.TestCase;
import com.jexunit.examples.arithmeticaltests.model.ArithmeticalTestObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple Test for the framework.
 * <p>
 * This test doesn't extend the base-class ({@link JExUnitBase}). This test works with the
 * <code>@RunWith(GevoTester.class)</code>-Annotation as the integration point for the framework.
 * </p>
 * <p>
 * This test should provide the arithmetical operations MUL and DIV. In this version the automatic object-creation /
 * -matching mechanism is tested. The test-command-methods get the testCase and a model-object as parameters. The
 * model-objects will automatically be created by the framework. Therefore the parameter-names in the excel-file will be
 * matched to the attribute-names of the model-class.
 * </p>
 * <p>
 * The test-command DIV will also be found in another class (that works as an ExcelCommandProvider), but in this test,
 * the method/test-command defined in the class itself should be used!
 * </p>
 * <p>
 * The operations ADD and SUB will be provided by another ExcelCommandProvider.
 * </p>
 * <p>
 * As add-on, there is an additional "normal" JUnit-Test(-Method), to test, if this method won't be ignored!
 * </p>
 *
 * @author fabian
 */
public class ArithmeticalTest {

    private static final Logger log = Logger.getLogger(ArithmeticalTest.class.getName());

    @TestFactory
    Object test() {
        return JExUnitBase.builder()
                .path("src/test/resources/ArithmeticalTests.xlsx")
                .path("src/test/resources/ArithmeticalTests2.xlsx")
                .testType(ArithmeticalTestCommands.class)
                .testType(this.getClass())
                .build().register();
    }

    @BeforeAll
    public static void configure() {
        JExUnitConfig.setConfigProperty("mytest.configkey", "test-value");
    }

    @BeforeEach
    public void init() {
        log.log(Level.INFO, "BeforeClass - ArithmeticTests");
    }

    @Test
    public void testConfiguration() {
        // "Default configuration should be overridden by the properties of the jexunit.properties"
        assertThat(JExUnitConfig.getStringProperty(JExUnitConfig.ConfigKey.DATE_PATTERN)).isEqualTo("MM/dd/yyyy");

        // "Properties set in the @BeforeClass should be accessible via the JExUnitConfig"
        assertThat(JExUnitConfig.getStringProperty("mytest.configkey")).isEqualTo("test-value");
    }

    @TestCommand("mul")
    public static void runMulCommand(final TestCase<?> testCase, final ArithmeticalTestObject testObject) {
        log.log(Level.INFO, "in test command: MUL!");
        assertThat(testObject.getParam1() * testObject.getParam2()).isEqualTo(testObject.getResult());
    }

    @TestCommand("div")
    public static void runDivCommand(final TestCase<?> testCase, final ArithmeticalTestObject testObject) {
        log.log(Level.INFO, "in test command: DIV!");
        assertThat(testObject.getParam1() / testObject.getParam2()).isEqualTo(testObject.getResult());
    }

    @Test
    public void simpleTest() {
        log.info("What about this test?");
        assertThat(JExUnitConfig.getStringProperty("mytest.configkey")).isEqualTo("test-value");
    }

}
