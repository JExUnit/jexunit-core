package com.jexunit.core.spi.data;

import com.jexunit.core.model.TestCase;

import java.util.Collection;

/**
 * DataProvider interface to read the data from file or anywhere and transform it to the internal representation.<br>
 * The ServiceRegistry will find all implementations of DataProviders. At runtime the JExUnit framework will "ask" the
 * data providers, if they can provide some data for a given test class. So for example you can define your own
 * annotations to check for in your data provider.
 *
 * @author fabian
 */
public interface DataProvider {

    /**
     * Load the test data and transform it into JExUnits internal representation.<br>
     * Each object has to be a list of type {@link TestCase}.
     *
     * @param test the number of the test to load the data for
     * @return the list of TestCases
     * @throws Exception if something went wrong
     */
    Collection<Object[]> loadTestData(final int test) throws Exception;

}
