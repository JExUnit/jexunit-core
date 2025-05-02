package com.jexunit.examples.businesstests.commands;

import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.context.Context;
import com.jexunit.core.data.TestObjectHelper;
import com.jexunit.core.model.TestCase;
import com.jexunit.core.model.TestCell;
import com.jexunit.examples.businesstests.entity.MyComplexBusinessEntity;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Test-Command implementation. This implements the command: COMPARE. This command will get the current entity out of
 * the TestContext and compare it with the properties out of the excel-file (TestCase). The JExUnit-Framework will be
 * used to "inject" the objects needed.
 */
@TestCommand("compare")
@SuppressWarnings("unused")
public class CompareTestCommand {

    /**
     * This is an example implementation for a reusable compare test-command checking all the values set in the test
     * (excel-file).
     *
     * @param testCase the current testCase
     * @param actual   the current business entity
     * @throws Exception if any error occurs during comparison
     */
    public void compare(@Context final TestCase<?> testCase,
                        @Context final MyComplexBusinessEntity actual) throws Exception {
        for (final Map.Entry<String, TestCell> entry : testCase.getValues().entrySet()) {
            final Object obj = TestObjectHelper.getProperty(actual, entry.getKey());
            final Object expected = TestObjectHelper.convertPropertyStringToObject(obj.getClass(),
                    entry.getValue().getValue());

            if (obj instanceof BigDecimal && expected instanceof BigDecimal) {
                Assertions.assertEquals(0, ((BigDecimal) obj).compareTo((BigDecimal) expected));
            } else {
                Assertions.assertEquals(expected, obj);
            }
        }
    }

}
