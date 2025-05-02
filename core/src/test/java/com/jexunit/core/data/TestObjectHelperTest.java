package com.jexunit.core.data;

import com.jexunit.core.data.entity.TestEnum;
import com.jexunit.core.data.entity.TestModelBase;
import com.jexunit.core.model.TestCase;
import com.jexunit.core.model.TestCell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestObjectHelperTest {

    private static final Map<String, TestCell> testValuesBase = new HashMap<>();
    private static final Map<String, TestCell> testValuesSubElement = new HashMap<>();
    private static final Map<String, TestCell> testValuesSubElement2 = new HashMap<>();
    private static final Map<String, TestCell> testValuesList = new HashMap<>();
    private static final Map<String, TestCell> testValuesList2 = new HashMap<>();

    @BeforeAll
    public static void prepare() {
        testValuesBase.put("intAttr", new TestCell("A", "5"));
        testValuesBase.put("doubleAttr", new TestCell("B", "3.21"));
        testValuesBase.put("stringAttr", new TestCell("C", "Test String"));
        testValuesBase.put("booleanAttr", new TestCell("D", "true"));
        testValuesBase.put("stringAttr2", new TestCell("E", "second test string"));
        testValuesBase.put("enumAttr", new TestCell("F", "TYPE_B"));

        testValuesSubElement.put("subEntityAttr.stringAttr", new TestCell("G", "sub entity test string"));
        testValuesSubElement.put("subEntityAttr.intAttr", new TestCell("H", "38"));
        testValuesSubElement.put("subEntityAttr.boolAttr", new TestCell("I", "true"));
        testValuesSubElement.put("subEntityAttr.enumAttr", new TestCell("J", "TYPE_C"));

        testValuesList.put("subEntityListAttr[0].intAttr", new TestCell("K", "1"));
        testValuesList.put("subEntityListAttr[1].intAttr", new TestCell("L", "2"));
        testValuesList.put("subEntityListAttr[0].boolAttr", new TestCell("M", "true"));

        testValuesSubElement2.put("subEntityAttr2.stringAttr", new TestCell("T", "sub entity test string"));
        testValuesSubElement2.put("subEntityAttr2.intAttr", new TestCell("U", "38"));
        testValuesSubElement2.put("subEntityAttr2.boolAttr", new TestCell("V", "true"));
        testValuesSubElement2.put("subEntityAttr2.enumAttr", new TestCell("W", "TYPE_C"));

        testValuesList2.put("subEntityListAttr2[0].intAttr", new TestCell("X", "1"));
        testValuesList2.put("subEntityListAttr2[1].intAttr", new TestCell("Y", "2"));
        testValuesList2.put("subEntityListAttr2[0].boolAttr", new TestCell("Z", "true"));
    }

    @Test
    public void testCreateObjectTestCaseClassOfT_baseValues() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesBase);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, TestModelBase.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Test String", actual.getStringAttr());
        Assertions.assertEquals(5, actual.getIntAttr());
        Assertions.assertEquals(3.21, actual.getDoubleAttr());
        Assertions.assertTrue(actual.isBooleanAttr());
        Assertions.assertEquals("second test string", actual.getStringAttr2());
        Assertions.assertEquals(TestEnum.TYPE_B, actual.getEnumAttr());
    }

    @Test
    public void testCreateObjectTestCaseClassOfT_subElementValues() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesSubElement);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, TestModelBase.class);

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.getSubEntityAttr().isBoolAttr());
        Assertions.assertEquals(38, actual.getSubEntityAttr().getIntAttr());
        Assertions.assertEquals("sub entity test string", actual.getSubEntityAttr().getStringAttr());
        Assertions.assertEquals(TestEnum.TYPE_C, actual.getSubEntityAttr().getEnumAttr());
    }

    @Test
    public void testCreateObjectTestCaseClassOfT_subElementValues_creatingNewSubElement() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesSubElement2);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, TestModelBase.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getSubEntityAttr2());
        Assertions.assertTrue(actual.getSubEntityAttr2().isBoolAttr());
        Assertions.assertEquals(38, actual.getSubEntityAttr2().getIntAttr());
        Assertions.assertEquals("sub entity test string", actual.getSubEntityAttr2().getStringAttr());
        Assertions.assertEquals(TestEnum.TYPE_C, actual.getSubEntityAttr2().getEnumAttr());
    }

    @Test
    public void testCreateObjectTestCaseClassOfT_listValues() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesList);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, TestModelBase.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getSubEntityListAttr());
        Assertions.assertEquals(2, actual.getSubEntityListAttr().size());
        Assertions.assertEquals(1, actual.getSubEntityListAttr().get(0).getIntAttr());
        Assertions.assertTrue(actual.getSubEntityListAttr().get(0).isBoolAttr());
        Assertions.assertEquals(2, actual.getSubEntityListAttr().get(1).getIntAttr());
    }

    @Test
    public void testCreateObjectTestCaseClassOfT_listValues_creatingNewList() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesList2);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, TestModelBase.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getSubEntityListAttr2());
        Assertions.assertEquals(2, actual.getSubEntityListAttr2().size());
        Assertions.assertEquals(1, actual.getSubEntityListAttr2().get(0).getIntAttr());
        Assertions.assertTrue(actual.getSubEntityListAttr2().get(0).isBoolAttr());
        Assertions.assertEquals(2, actual.getSubEntityListAttr2().get(1).getIntAttr());
    }

    @Test
    public void testCreateObjectTestCaseT() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesBase);
        testCase.getValues().putAll(testValuesSubElement);
        testCase.getValues().putAll(testValuesList);
        testCase.getValues().remove("intAttr");

        final TestModelBase base = new TestModelBase();
        base.setIntAttr(-768);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, base);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Test String", actual.getStringAttr());
        Assertions.assertEquals(-768, actual.getIntAttr());
        Assertions.assertEquals(3.21, actual.getDoubleAttr());
        Assertions.assertTrue(actual.isBooleanAttr());
        Assertions.assertEquals("second test string", actual.getStringAttr2());
        Assertions.assertEquals(TestEnum.TYPE_B, actual.getEnumAttr());
        Assertions.assertTrue(actual.getSubEntityAttr().isBoolAttr());
        Assertions.assertEquals(38, actual.getSubEntityAttr().getIntAttr());
        Assertions.assertEquals("sub entity test string", actual.getSubEntityAttr().getStringAttr());
        Assertions.assertEquals(TestEnum.TYPE_C, actual.getSubEntityAttr().getEnumAttr());

        Assertions.assertNotNull(actual.getSubEntityListAttr());
        Assertions.assertEquals(2, actual.getSubEntityListAttr().size());
        Assertions.assertEquals(1, actual.getSubEntityListAttr().get(0).getIntAttr());
        Assertions.assertTrue(actual.getSubEntityListAttr().get(0).isBoolAttr());
        Assertions.assertEquals(2, actual.getSubEntityListAttr().get(1).getIntAttr());
    }

    @Test
    public void testCreateObjectTestCaseClassOfT_subListValues_creatingNewLists() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesList2);

        final Map<String, TestCell> testValuesSubList = new HashMap<>();
        testValuesSubList.put("subEntityListAttr2[0].subListAttr[0].intAttr", new TestCell("AD", "100"));
        testValuesSubList.put("subEntityListAttr2[1].subListAttr[0].intAttr", new TestCell("AE", "99"));
        testValuesSubList.put("subEntityListAttr2[0].subListAttr[1].boolAttr", new TestCell("AF", "true"));

        testCase.getValues().putAll(testValuesSubList);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, TestModelBase.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getSubEntityListAttr2());
        Assertions.assertEquals(2, actual.getSubEntityListAttr2().size());
        Assertions.assertEquals(1, actual.getSubEntityListAttr2().get(0).getIntAttr());
        Assertions.assertTrue(actual.getSubEntityListAttr2().get(0).isBoolAttr());
        Assertions.assertEquals(2, actual.getSubEntityListAttr2().get(1).getIntAttr());

        Assertions.assertNotNull(actual.getSubEntityListAttr2().get(0).getSubListAttr());
        Assertions.assertEquals(2, actual.getSubEntityListAttr2().get(0).getSubListAttr().size());
        Assertions.assertEquals(100, actual.getSubEntityListAttr2().get(0).getSubListAttr().get(0).getIntAttr());
        Assertions.assertTrue(actual.getSubEntityListAttr2().get(0).getSubListAttr().get(1).isBoolAttr());
        Assertions.assertEquals(1, actual.getSubEntityListAttr2().get(1).getSubListAttr().size());
        Assertions.assertEquals(99, actual.getSubEntityListAttr2().get(1).getSubListAttr().getFirst().getIntAttr());
    }

    @Test
    public void testCreateObjectTestCaseClassOfT_baseValuesAndMap() throws Exception {
        final TestCase<?> testCase = new TestCase<>();
        testCase.getValues().putAll(testValuesBase);

        final Map<String, TestCell> testValuesMap = new HashMap<>();
        testValuesMap.put("mapAttr[\"myKey\"]", new TestCell("AD", "Hello"));
        testValuesMap.put("mapAttr['yourKey']", new TestCell("AE", "world"));
        testValuesMap.put("mapAttr[\"ourKey\"]", new TestCell("AF", "yeah!"));

        testCase.getValues().putAll(testValuesMap);

        final TestModelBase actual = TestObjectHelper.createObject(testCase, TestModelBase.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Test String", actual.getStringAttr());
        Assertions.assertEquals(5, actual.getIntAttr());
        Assertions.assertEquals(3.21, actual.getDoubleAttr());
        Assertions.assertTrue(actual.isBooleanAttr());
        Assertions.assertEquals("second test string", actual.getStringAttr2());
        Assertions.assertEquals(TestEnum.TYPE_B, actual.getEnumAttr());

        Assertions.assertNotNull(actual.getMapAttr());
        Assertions.assertEquals(3, actual.getMapAttr().size());
        Assertions.assertEquals("Hello", actual.getMapAttr().get("myKey"));
        Assertions.assertEquals("world", actual.getMapAttr().get("yourKey"));
        Assertions.assertEquals("yeah!", actual.getMapAttr().get("ourKey"));
    }

}
