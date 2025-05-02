package com.jexunit.examples.arithmeticaltests;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.JExUnitConfig;
import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.model.TestCase;
import com.jexunit.examples.arithmeticaltests.model.ArithmeticalTestObject;
import org.junit.jupiter.api.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Test for the framework.
 */
public class ArithmeticalTest {

    private static final Logger log = Logger.getLogger(ArithmeticalTest.class.getName());
    private static final double DELTA = 0.000001;

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
        Assertions.assertEquals("MM/dd/yyyy", JExUnitConfig.getStringProperty(JExUnitConfig.ConfigKey.DATE_PATTERN));

        // "Properties set in the @BeforeClass should be accessible via the JExUnitConfig"
        Assertions.assertEquals("test-value", JExUnitConfig.getStringProperty("mytest.configkey"));
    }

    @TestCommand("mul")
    @SuppressWarnings("unused")
    public static void runMulCommand(final TestCase<?> testCase, final ArithmeticalTestObject testObject) {
        log.log(Level.INFO, "in test command: MUL!");
        double actual = testObject.getParam1() * testObject.getParam2();
        Assertions.assertEquals(testObject.getResult(), actual, DELTA);
    }

    @TestCommand("div")
    @SuppressWarnings("unused")
    public static void runDivCommand(final TestCase<?> testCase, final ArithmeticalTestObject testObject) {
        log.log(Level.INFO, "in test command: DIV!");
        double actual = testObject.getParam1() / testObject.getParam2();
        Assertions.assertEquals(testObject.getResult(), actual, DELTA);
    }

    @Test
    public void simpleTest() {
        log.info("What about this test?");
        Assertions.assertEquals("test-value", JExUnitConfig.getStringProperty("mytest.configkey"));
    }

}
