package com.jexunit.core.dataprovider;

import com.jexunit.core.spi.data.DataProvider;

import java.util.Collection;
import java.util.List;

/**
 * DataProvider implementation for reading the test data out of excel files.
 *
 * @author fabian
 */
public class ExcelDataProvider implements DataProvider {

    public ExcelDataProvider(boolean worksheetAsTest,
                             boolean transpose,
                             List<String> paths) {
        this.excelFileNames = paths;
        this.worksheetAsTest = worksheetAsTest;
        this.transpose = transpose;
    }

    // hold the information for multiple excel-files
    private final List<String> excelFileNames;

    private final boolean worksheetAsTest;

    private final boolean transpose;

    @Override
    public Collection<Object[]> loadTestData(final int test) throws Exception {
        if (excelFileNames == null || test >= excelFileNames.size() || test < 0) {
            throw new IllegalArgumentException("The ExcelDataProvider cannot provide test data for test number " + test
                    + "!");
        }
        final ExcelLoader excelLoader = new ExcelLoader(worksheetAsTest, transpose);
        return excelLoader.loadTestData(excelFileNames.get(test));
    }

}
