package com.jexunit.core.dataprovider;

import com.jexunit.core.model.TestCase;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelLoaderTest {

    @Test
    public void type() throws Exception {
        assertThat(ExcelLoader.class).isNotNull();
    }

    @Test
    public void shouldReadExcel() throws Exception {
        final ExcelLoader target = new ExcelLoader();
        final Map<String, List<TestCase<ExcelMetadata>>> data = target.readExcel(Paths.get("", "src", "test", "resources", "loader-test.xlsx").toAbsolutePath().toString());
        assertThat(data.size()).isEqualTo(1);
        final List<TestCase<ExcelMetadata>> testCases = data.get("worksheet1");
        assertThat(testCases).isNotNull();
        assertThat(testCases.size()).isEqualTo(3);
        assertThat(testCases.get(0).getTestCommand()).isEqualTo("test");
        assertThat(testCases.get(0).getValues().size()).isEqualTo(4);
    }

    @Test
    public void shouldReadExcelTransposed() throws Exception {
        final ExcelLoader target = new ExcelLoader(false, true);
        final Map<String, List<TestCase<ExcelMetadata>>> data = target.readExcel(Paths.get("", "src", "test", "resources", "loader-test-transpose.xlsx").toAbsolutePath().toString());
        assertThat(data.size()).isEqualTo(1);
        final List<TestCase<ExcelMetadata>> testCases = data.get("worksheet1");
        assertThat(testCases).isNotNull();
        assertThat(testCases.size()).isEqualTo(3);
        assertThat(testCases.get(0).getTestCommand()).isEqualTo("test");
        assertThat(testCases.get(0).getValues().size()).isEqualTo(4);
    }

}
