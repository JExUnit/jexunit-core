package com.jexunit.examples.features;

import com.jexunit.core.dataprovider.ExcelFile;
import org.junit.jupiter.api.Disabled;

@Disabled("How to test the fast fail feature?")
public class FeaturesTest {

	@ExcelFile
	static String[] excelFiles = new String[] { "src/test/resources/FeaturesTest.xlsx" };

}
