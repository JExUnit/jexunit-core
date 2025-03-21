package com.jexunit.examples.businesstests;

import com.jexunit.core.dataprovider.ExcelFile;

/**
 * This is a more complex example for the JExUnit-Framework using as much features as possible! (see
 * the TestCommand-Implementations).
 * 
 * @author fabian
 * 
 */
public class MyComplexBusinessTest {

	@ExcelFile
	private static final String[] excelFiles = new String[] { "src/test/resources/ComplexBusinessTest.xlsx" };

}
