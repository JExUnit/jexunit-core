package com.jexunit.examples.arithmeticaltests;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.model.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestFactory;

import java.util.logging.Logger;

/**
 * Simple Test for the framework.
 * <p>
 * This test should provide the excel file via the static method {@link #getExcelFile()} annotated with
 * <code>&#064;ExcelFile</code>.
 * </p>
 * <p>
 * All the test-commands, that are not found in the classpath (methods annotated with the <code>@TestCommand</code>
 * -Annotation), will be handled by the overridden {@link #runCommand(TestCase)} method.
 * </p>
 *
 * @author fabian
 */
public class ArithmeticalMethodTest {

    private static final Logger log = Logger.getLogger(ArithmeticalMethodTest.class.getName());

    @BeforeAll
    public static void setup() {
        log.info("BeforeClass - ArithmeticTests");
    }

    @BeforeEach
    public void init() {
        log.info("Before - ArithmeticTests");
    }

    @TestFactory
    Object test() {
        return JExUnitBase.builder()
                .path("src/test/resources/ArithmeticalTests.xlsx")
                .testType(ArithmeticalTest.class)
                .testType(ArithmeticalTestCommands.class)
                .build().register();
    }

}
