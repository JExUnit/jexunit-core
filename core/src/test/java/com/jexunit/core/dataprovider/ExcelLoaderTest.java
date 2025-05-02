package com.jexunit.core.dataprovider;

import com.jexunit.core.model.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class ExcelLoaderTest {

    @Test
    public void type() {
        Assertions.assertNotNull(ExcelLoader.class);
    }

    @Test
    public void shouldReadExcel() throws Exception {
        final ExcelLoader target = new ExcelLoader();
        final Map<String, List<TestCase<ExcelMetadata>>> data = target.readExcel(
                Paths.get("", "src", "test", "resources", "loader-test.xlsx")
                        .toAbsolutePath().toString()
        );

        Assertions.assertEquals(1, data.size());

        final List<TestCase<ExcelMetadata>> testCases = data.get("worksheet1");

        Assertions.assertNotNull(testCases);
        Assertions.assertEquals(3, testCases.size());
        Assertions.assertEquals("test", testCases.getFirst().getTestCommand());
        Assertions.assertEquals(4, testCases.getFirst().getValues().size());
    }

    @Test
    public void shouldReadExcelTransposed() throws Exception {
        final ExcelLoader target = new ExcelLoader(false, true);
        final Map<String, List<TestCase<ExcelMetadata>>> data = target.readExcel(
                Paths.get("", "src", "test", "resources", "loader-test-transpose.xlsx")
                        .toAbsolutePath().toString()
        );

        Assertions.assertEquals(1, data.size());

        final List<TestCase<ExcelMetadata>> testCases = data.get("worksheet1");

        Assertions.assertNotNull(testCases);
        Assertions.assertEquals(3, testCases.size());
        Assertions.assertEquals("test", testCases.getFirst().getTestCommand());
        Assertions.assertEquals(4, testCases.getFirst().getValues().size());
    }

}
