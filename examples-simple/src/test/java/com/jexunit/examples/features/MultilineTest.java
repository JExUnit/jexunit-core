package com.jexunit.examples.features;

import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.dataprovider.ExcelFile;
import com.jexunit.core.model.TestCase;
import com.jexunit.core.model.TestCell;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MultilineTest {

    @ExcelFile
    static String[] excelFiles = new String[]{"src/test/resources/MultilineTest.xlsx"};

    private static List<Map<String, TestCell>> multilineValues;
    private static List<Map<String, TestCell>> singleLineValues;
    private static Map<String, TestCell> singleLineValue;
    private static List<Map<String, TestCell>> defaultMultilineValues;

    @TestCommand("createPerson")
    public static void createPerson(final TestCase<?> testCase) {
        if (testCase.isMultiline()) {
            multilineValues = testCase.getMultilineValues();
        } else {
            singleLineValues = testCase.getMultilineValues();
            singleLineValue = testCase.getValues();
        }
    }

    @TestCommand("createPersonMultiline")
    public static void createPersonMultiline(final TestCase<?> testCase) {
        assertThat(testCase.isMultiline()).isTrue();
        defaultMultilineValues = testCase.getMultilineValues();
    }

    @Test
    public void testMultilineValues() {
        // Drei Multiline-Zeilen hintereinander
        assertThat(multilineValues.size()).isEqualTo(3);

        // Sortierung wie im Excel
        assertThat(multilineValues.get(0).get("firstname").getValue()).isEqualTo("Max");
        assertThat(multilineValues.get(1).get("firstname").getValue()).isEqualTo("Manfred");
        assertThat(multilineValues.get(2).get("firstname").getValue()).isEqualTo("Rudi");

        // Multiline-Spalte selbst ist nicht aufgeführt
        assertThat(multilineValues.get(0).values().size()).isEqualTo(4);
        assertThat(multilineValues.get(1).values().size()).isEqualTo(4);
        assertThat(multilineValues.get(2).values().size()).isEqualTo(4);
    }

    @Test
    public void testDefaultMultilineValues() {
        // Drei Multiline-Zeilen hintereinander
        assertThat(defaultMultilineValues.size()).isEqualTo(3);


        // Sortierung wie im Excel
        assertThat(defaultMultilineValues.get(0).get("firstname").getValue()).isEqualTo("Robert");
        assertThat(defaultMultilineValues.get(1).get("firstname").getValue()).isEqualTo("Simon");
        assertThat(defaultMultilineValues.get(2).get("firstname").getValue()).isEqualTo("Julian");

        // Multiline-Spalte selbst ist nicht aufgeführt
        assertThat(defaultMultilineValues.get(0).values().size()).isEqualTo(3);
        assertThat(defaultMultilineValues.get(1).values().size()).isEqualTo(3);
        assertThat(defaultMultilineValues.get(2).values().size()).isEqualTo(3);
    }

    @Test
    public void testSinglelineValues() {
        // Multiline-Zugriff auch bei Single-Line möglich
        assertThat(singleLineValues.size()).isEqualTo(1);

        // Letzter Eintrag ist der aktuelle
        assertThat(singleLineValues.get(0).get("firstname").getValue()).isEqualTo("Roberta");

        // Multiline-Zeiger zeigt auf aktuellen Wert
        assertThat(singleLineValues.get(0)).isEqualTo(singleLineValue);

        // Multiline-Spalte selbst ist nicht aufgeführt
        assertThat(multilineValues.get(0).values().size()).isEqualTo(4);
    }

}
