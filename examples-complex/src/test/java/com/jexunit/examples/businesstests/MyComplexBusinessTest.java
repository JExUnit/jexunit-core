package com.jexunit.examples.businesstests;

import com.jexunit.core.JExUnitBase;
import org.junit.jupiter.api.TestFactory;

/**
 * This is a more complex example for the JExUnit-Framework using as much features as possible! (see the
 * TestCommand-Implementations).
 *
 * @author fabian
 */
public class MyComplexBusinessTest {

    @TestFactory
    Object test() {
        return JExUnitBase.builder()
                .path("src/test/resources/ComplexBusinessTest.xlsx")
                .build().register();
    }
}
