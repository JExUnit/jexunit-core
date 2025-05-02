package com.jexunit.examples.features;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.model.TestCase;
import com.jexunit.core.model.TestCell;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MultilineTest {

    private static List<Map<String, TestCell>> multilineValues;
    private static List<Map<String, TestCell>> singleLineValues;
    private static Map<String, TestCell> singleLineValue;
    private static List<Map<String, TestCell>> defaultMultilineValues;

    @TestFactory
    @Order(1)
    Object testMultiline() {
        return JExUnitBase.builder().paths(List.of("src/test/resources/MultilineTest.xlsx")).build().register();
    }

    @TestCommand("createPerson")
    @SuppressWarnings("unused")
    public static void createPerson(final TestCase<?> testCase) {
        if (testCase.isMultiline()) {
            multilineValues = testCase.getMultilineValues();
        } else {
            singleLineValues = testCase.getMultilineValues();
            singleLineValue = testCase.getValues();
        }
    }

    @TestCommand("createPersonMultiline")
    @SuppressWarnings("unused")
    public static void createPersonMultiline(final TestCase<?> testCase) {
        Assertions.assertTrue(testCase.isMultiline());
        defaultMultilineValues = testCase.getMultilineValues();
    }

    @Test
    @Order(2)
    public void testMultilineValues() {
        // Drei Multiline-Zeilen hintereinander
        Assertions.assertEquals(3, multilineValues.size());

        // Sortierung wie im Excel
        Assertions.assertEquals("Max", multilineValues.get(0).get("firstname").getValue());
        Assertions.assertEquals("Manfred", multilineValues.get(1).get("firstname").getValue());
        Assertions.assertEquals("Rudi", multilineValues.get(2).get("firstname").getValue());

        // Multiline-Spalte selbst ist nicht aufgeführt
        Assertions.assertEquals(4, multilineValues.get(0).values().size());
        Assertions.assertEquals(4, multilineValues.get(1).values().size());
        Assertions.assertEquals(4, multilineValues.get(2).values().size());
    }

    @Test
    @Order(3)
    public void testDefaultMultilineValues() {
        // Drei Multiline-Zeilen hintereinander
        Assertions.assertEquals(3, defaultMultilineValues.size());

        // Sortierung wie im Excel
        Assertions.assertEquals("Robert", defaultMultilineValues.get(0).get("firstname").getValue());
        Assertions.assertEquals("Simon", defaultMultilineValues.get(1).get("firstname").getValue());
        Assertions.assertEquals("Julian", defaultMultilineValues.get(2).get("firstname").getValue());

        // Multiline-Spalte selbst ist nicht aufgeführt
        Assertions.assertEquals(3, defaultMultilineValues.get(0).values().size());
        Assertions.assertEquals(3, defaultMultilineValues.get(1).values().size());
        Assertions.assertEquals(3, defaultMultilineValues.get(2).values().size());
    }

    @Test
    @Order(4)
    public void testSinglelineValues() {
        // Multiline-Zugriff auch bei Single-Line möglich
        Assertions.assertEquals(1, singleLineValues.size());

        // Letzter Eintrag ist der aktuelle
        Assertions.assertEquals("Roberta", singleLineValues.getFirst().get("firstname").getValue());

        // Multiline-Zeiger zeigt auf aktuellen Wert
        Assertions.assertEquals(singleLineValue, singleLineValues.getFirst());

        // Multiline-Spalte selbst ist nicht aufgeführt
        Assertions.assertEquals(4, multilineValues.getFirst().values().size());
    }

}
