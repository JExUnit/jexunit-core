package com.jexunit.examples.arithmeticaltests;

import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.data.TestObjectHelper;
import com.jexunit.core.model.TestCase;
import com.jexunit.examples.arithmeticaltests.model.ArithmeticalTestObject;
import org.junit.jupiter.api.Assertions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provide the Test-Commands for the Arithmetical tests.
 */
public class ArithmeticalTestCommands {

    private static final Logger log = Logger.getLogger(ArithmeticalTestCommands.class.getName());

    public static void runCommand(final TestCase<?> testCase) throws Exception {
        final Double val1 = Double.parseDouble(testCase.getValues().get("param1").getValue());
        final Double val2 = Double.parseDouble(testCase.getValues().get("param2").getValue());
        final Double result = Double.parseDouble(testCase.getValues().get("result").getValue());

        log.log(Level.INFO, "run command (testCase: {0})", testCase);

        switch (testCase.getTestCommand()) {
            case "ADD":
                Assertions.assertEquals(result, val1 + val2);
                break;
            case "SUB":
            case "SUBTRACT":
                Assertions.assertEquals(result, val1 - val2);
                break;
            case "MUL":
                Assertions.assertEquals(result, val1 * val2);
                break;
            case "DIV":
                Assertions.assertEquals(result, val1 / val2);
                break;
        }
    }

    public static void runCommandWithObject(final TestCase<?> testCase) throws Exception {
        final ArithmeticalTestObject obj = TestObjectHelper.createObject(testCase, ArithmeticalTestObject.class);

        log.log(Level.INFO, "run command with object (testCase: {0})", testCase);

        switch (testCase.getTestCommand()) {
            case "ADD":
                Assertions.assertEquals(obj.getResult(), obj.getParam1() + obj.getParam2());
                break;
            case "SUB":
            case "SUBTRACT":
                Assertions.assertEquals(obj.getResult(), obj.getParam1() - obj.getParam2());
                break;
            case "MUL":
                Assertions.assertEquals(obj.getResult(), obj.getParam1() * obj.getParam2());
                break;
            case "DIV":
                Assertions.assertEquals(obj.getResult(), obj.getParam1() / obj.getParam2());
                break;
        }
    }

    @TestCommand("add")
    @SuppressWarnings("unused")
    public void runAddCommand(final TestCase<?> testCase) throws Exception {
        log.info("in test command: ADD!");
        final ArithmeticalTestObject obj = TestObjectHelper.createObject(testCase, ArithmeticalTestObject.class);
        Assertions.assertEquals(obj.getResult(), obj.getParam1() + obj.getParam2());
    }

    @TestCommand({"sub", "subtract"})
    @SuppressWarnings("unused")
    public void runSubCommand(final TestCase<?> testCase, final ArithmeticalTestObject testObject) {
        log.info("in test command: " + testCase.getTestCommand() + "!");
        Assertions.assertEquals(testObject.getResult(), testObject.getParam1() - testObject.getParam2());
    }

}
