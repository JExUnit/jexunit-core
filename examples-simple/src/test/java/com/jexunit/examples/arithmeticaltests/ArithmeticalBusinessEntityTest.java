package com.jexunit.examples.arithmeticaltests;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.commands.annotation.TestCommand.TestCommands;
import com.jexunit.examples.arithmeticaltests.model.CustomTestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Test for the framework.
 * <p>
 * This test doesn't extend the base-class ({@link JExUnitBase}). This test works with the
 * <code>@RunWith(GevoTester.class)</code>-Annotation as the integration point for the framework.
 * </p>
 * <p>
 * This test should provide all the arithmetical operations ADD, SUB, MUL and DIV. In this version
 * the automatic object-creation / -matching mechanism is tested. The test-command-methods get the
 * testCase and a model-object as parameters. The model-objects will automatically be created by the
 * framework. Therefore the parameter-names in the excel-file will be matched to the attribute-names
 * of the model-class. This will test the construction of (model-)object trees, to separate the
 * "test-entities" from the business-entities.
 * </p>
 */
public class ArithmeticalBusinessEntityTest {

    private static final Logger log = Logger.getLogger(ArithmeticalBusinessEntityTest.class.getName());
    private static final double DELTA = 0.000001;

    @TestFactory
    Object test() {
        return JExUnitBase.builder()
                .testType(this.getClass())
                .path("src/test/resources/ArithmeticalBusinessEntityTests.xlsx")
                .build().register();
    }

    @TestCommand("add")
    @SuppressWarnings("unused")
    public static void runAddCommand(final CustomTestObject testObject) {
        log.log(Level.INFO, "in test command: ADD!");
        double actual = testObject.getEntity().getParam1() + testObject.getEntity().getParam2();
        Assertions.assertEquals(testObject.getResult(), actual, DELTA);
    }

    @TestCommand("sub")
    @SuppressWarnings("unused")
    public static void runSubCommand(final CustomTestObject testObject) {
        log.log(Level.INFO, "in test command: SUB!");
        double actual = testObject.getEntity().getParam1() - testObject.getEntity().getParam2();
        Assertions.assertEquals(testObject.getResult(), actual, DELTA);
    }

    @TestCommand("multiply")
    @TestCommand("mul")
    @SuppressWarnings("unused")
    public static void runMulCommand(final CustomTestObject testObject) {
        log.log(Level.INFO, "in test command: MUL!");
        double actual = testObject.getEntity().getParam1() * testObject.getEntity().getParam2();
        Assertions.assertEquals(testObject.getResult(), actual, DELTA);
    }

    @TestCommands({@TestCommand("divide"), @TestCommand("div")})
    @SuppressWarnings("unused")
    public static void runDivCommand(final CustomTestObject testObject) {
        log.log(Level.INFO, "in test command: DIV!");
        double actual = testObject.getEntity().getParam1() / testObject.getEntity().getParam2();
        Assertions.assertEquals(testObject.getResult(), actual, DELTA);
    }

}
