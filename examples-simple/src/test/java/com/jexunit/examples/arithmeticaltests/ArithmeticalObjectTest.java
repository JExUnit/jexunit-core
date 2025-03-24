package com.jexunit.examples.arithmeticaltests;

import com.jexunit.core.JExUnitBase;
import org.junit.jupiter.api.TestFactory;

import java.util.List;

/**
 * Simple Test for the framework. This test should provide the arithmetical operations ADD and SUB.
 *
 * @author fabian
 */
public class ArithmeticalObjectTest {

    @TestFactory
    Object test() {
        return JExUnitBase.builder()
                .transpose(true)
                .paths(List.of("src/test/resources/ArithmeticalTests.xlsx"))
                .build().register();
    }
}
