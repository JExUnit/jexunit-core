package com.jexunit.examples.features;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.JExUnitConfig;
import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.model.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FormattingTest {

    @TestFactory
    Object test() {
        return JExUnitBase.builder().path("src/test/resources/FormattingTest.xlsx").build().register();
    }

    @TestCommand("map")
    @SuppressWarnings("unused")
    public static void runMap(final TestCase<?> testCase, final HashMap<String, String> params) {
        final String param1 = params.get("param1");
        final String param2 = params.get("param2");

        // Random Int generator is evaluated exactly once
        Assertions.assertEquals(param2, param1);

        // Map key without value is truncated
        Assertions.assertFalse(params.containsKey("param3"));

        // param3 as well as empty date formatted cell in G6 is ignored
        Assertions.assertEquals(2, params.size());
    }

    @TestCommand("COMPAREDATE")
    @SuppressWarnings("unused")
    public static void run(final HashMap<String, String> params) {
        String date = params.get("date");
        String format = new SimpleDateFormat(JExUnitConfig.getStringProperty(JExUnitConfig.ConfigKey.DATE_PATTERN))
                .format(new Date());

        Assertions.assertEquals(format, date);

        date = params.get("timestamp");
        format = new SimpleDateFormat(JExUnitConfig.getStringProperty(JExUnitConfig.ConfigKey.DATETIME_PATTERN))
                .format(new Date());

        // Compare two timestamps (Excel and Java) with minor delta
        Assertions.assertEquals(
                date.substring(0, date.length() - 2),
                format.substring(0, format.length() - 2)
        );
    }

    @TestCommand("COMPAREINT")
    @SuppressWarnings("unused")
    public static void compareInt(final HashMap<String, String> params) {
        final String integer = params.get("integer");
        Assertions.assertEquals("1", integer);

        final String aFloat = params.get("float");
        Assertions.assertEquals("1.999", aFloat);
    }

}
