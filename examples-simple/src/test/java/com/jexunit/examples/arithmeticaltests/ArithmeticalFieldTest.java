package com.jexunit.examples.arithmeticaltests;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.model.TestCase;
import com.jexunit.examples.arithmeticaltests.model.ArithmeticalTestObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestFactory;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple Test for the framework.<br>
 * <p>
 * In this test, the excel file will be provided via the static String attribute "excelFile" annotated with
 * <code>@ExcelFile</code>.
 * </p>
 * <p>
 * This test should provide the arithmetical operation DIV. This method should be preferred if there are multiple
 * methods found for the test-command "DIV".
 * </p>
 * <p>
 * All the other test-commands will be handled by the overridden {@link #runCommand(TestCase)} method.
 * </p>
 *
 * @author fabian
 */
public class ArithmeticalFieldTest {

    private static final Logger log = Logger.getLogger(ArithmeticalFieldTest.class.getName());

    static String excelFile = "src/test/resources/ArithmeticalTests.xlsx";

    @BeforeAll
    public static void setup() {
        log.info("BeforeClass - ArithmeticTests");
    }

    @BeforeEach
    public void init() {
        log.info("Before - ArithmeticTests");
    }

    @TestFactory
    Object testArithmeticalField() {
        return JExUnitBase.builder()
                .testType(this.getClass())
                .testType(ArithmeticalTest.class)
                .testType(ArithmeticalTestCommands.class)
                .path(excelFile).build().register();
    }

    @TestCommand(value = "div")
    public static void runDivCommand(final TestCase<?> testCase, final ArithmeticalTestObject testObject) {
        log.info("in test command: DIV!");
        assertThat(testObject.getParam1() / testObject.getParam2()).isEqualTo(testObject.getResult());
    }

}
