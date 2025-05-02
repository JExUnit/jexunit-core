package com.jexunit.core;

import com.jexunit.core.commands.DefaultCommands;
import com.jexunit.core.commands.TestCommandRunner;
import com.jexunit.core.commands.TestCommandScanner;
import com.jexunit.core.commands.validation.CommandValidator;
import com.jexunit.core.context.TestContextManager;
import com.jexunit.core.dataprovider.ExcelDataProvider;
import com.jexunit.core.model.Metadata;
import com.jexunit.core.model.TestCase;
import com.jexunit.core.model.TestCell;
import com.jexunit.core.spi.AfterSheet;
import com.jexunit.core.spi.BeforeSheet;
import com.jexunit.core.spi.data.DataProvider;
import eu.infomas.annotation.AnnotationDetector;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

@Builder
public class JExUnitBase {

    private static final Logger log = Logger.getLogger(JExUnitBase.class.getName());

    @Getter
    @Singular
    private List<Class<?>> testTypes;

    @Getter
    @Builder.Default
    private boolean worksheetAsTest = true;

    @Getter
    @Builder.Default
    private boolean transpose = false;

    @Getter
    @Singular(ignoreNullCollections = true)
    private List<String> paths;

    private final TestCommandRunner testCommandRunner = new TestCommandRunner(this);

    static {
        final AnnotationDetector detector = new AnnotationDetector(new TestCommandScanner());
        try {
            JExUnitConfig.init();
            final String property = JExUnitConfig.getStringProperty(JExUnitConfig.ConfigKey.ANNOTATION_SCAN_PACKAGE);
            if (property != null && !property.isEmpty()) {
                detector.detect(property.split(","));
            } else {
                detector.detect();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public Stream<DynamicNode> register() {
        try {
            ExcelDataProvider instance = new ExcelDataProvider(worksheetAsTest, transpose, paths);
            TestContextManager.add(DataProvider.class, instance);
            Collection<Object[]> excelFiles = new ArrayList<>();
            for (int i = 0; i < paths.size(); i++) {
                excelFiles.addAll(setUp(i));
            }
            return excelFiles.stream()
                    .map(this::getDynamicContainer)
                    .filter(Objects::nonNull);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private DynamicNode getDynamicContainer(Object[] o) {
        List<TestCase<?>> testCase = (List<TestCase<?>>) o[0];
        if (testCase.isEmpty()) {
            return null;
        }
        Metadata metadata = testCase.getFirst().getMetadata();
        String label = metadata.getTestGroupName();

        return DynamicContainer.dynamicContainer(
                label,
                List.of(DynamicTest.dynamicTest(
                        metadata.getTestExecutionName(),
                        () -> executeTestCase(testCase)
                ))
        );
    }

    public static Collection<Object[]> setUp(final int testNumber) throws Exception {
        final DataProvider dataProvider = TestContextManager.get(DataProvider.class);
        final Collection<Object[]> testData = dataProvider.loadTestData(testNumber);
        CommandValidator.validateCommands(testData);
        return testData;
    }

    public void executeTestCase(List<TestCase<?>> testCases1) {
        List<Executable> assertions = new ArrayList<>();
        if (testCases1 == null || testCases1.isEmpty()) {
            return;
        }

        log.log(Level.INFO, "Running TestCase: {0}", testCases1.get(0).getMetadata().getTestGroup());

        testCaseLoop:
        for (final TestCase<?> testCase : testCases1) {
            final boolean exceptionExpected = testCase.isExceptionExpected();
            try {
                if (JExUnitConfig.getDefaultCommandProperty(DefaultCommands.DISABLED)
                        .equalsIgnoreCase(testCase.getTestCommand())) {
                    if (testCase.isDisabled()) {
                        log.info(String.format("Testsheet disabled! (%s)", testCase.getMetadata().getDetailedIdentifier()));
                        Assumptions.assumeTrue(true, String.format("Testsheet disabled! (%s)", testCase.getMetadata().getDetailedIdentifier()));
                        return;
                    }
                } else if (JExUnitConfig.getDefaultCommandProperty(DefaultCommands.REPORT)
                        .equalsIgnoreCase(testCase.getTestCommand())) {
                    for (final TestCell tc : testCase.getValues().values()) {
                        log.info(tc.getValue());
                    }
                    continue testCaseLoop;
                } else {
                    try {
                        if (testCase.isDisabled()) {
                            log.info(String.format("Testcase disabled! (command: %s, %s) %s",
                                    testCase.getTestCommand(), testCase.getMetadata().getDetailedIdentifier(), testCase.getComment()));
                            Assumptions.assumeTrue(true, String.format("Testcase disabled! (command: %s, %s) %s",
                                    testCase.getTestCommand(), testCase.getMetadata().getDetailedIdentifier(), testCase.getComment()));
                            continue testCaseLoop;
                        }
                        testCommandRunner.runTestCommand(testCase);
                    } catch (final AssertionError e) {
                        if (!exceptionExpected) {
                            assertions.add(() -> fail(String.format(
                                    "No Exception expected in TestCommand: %s, %s. %s",
                                    testCase.getTestCommand(), testCase.getMetadata().getDetailedIdentifier(), testCase.getComment()), e));
                            if (testCase.isFastFail()) {
                                assertions.add(() -> fail("FastFail attribute forces the complete test sheet to fail."));
                                break;
                            }
                        } else {
                            continue testCaseLoop;
                        }
                    } catch (final Exception e) {
                        Throwable t = e;
                        while ((t = t.getCause()) != null) {
                            if (t instanceof AssertionError) {
                                if (!exceptionExpected) {
                                    Throwable finalT = t;
                                    assertions.add(() -> fail(String.format(
                                            "No Exception expected in TestCommand: %s, %s. %s",
                                            testCase.getTestCommand(), testCase.getMetadata().getDetailedIdentifier(), testCase.getComment()), finalT));
                                    if (testCase.isFastFail()) {
                                        assertions.add(() -> fail("FastFail attribute forces the complete test sheet to fail."));
                                        break testCaseLoop;
                                    }
                                } else {
                                    continue testCaseLoop;
                                }
                            }
                        }
                        e.printStackTrace();
                        fail(String.format("Unexpected Exception thrown in TestCommand: %s, %s. (Exception: %s) %s",
                                testCase.getTestCommand(), testCase.getMetadata().getDetailedIdentifier(), e, testCase.getComment()));
                    }
                }

                if (exceptionExpected) {
                    assertions.add(() -> fail(String.format(
                            "Exception expected! in TestCommand: %s, %s. %s",
                            testCase.getTestCommand(), testCase.getMetadata().getDetailedIdentifier(), testCase.getComment())));
                    if (testCase.isFastFail()) {
                        log.log(Level.FINE, "FastFail activated");
                        assertions.add(() -> fail("FastFail attribute forces the complete test sheet to fail."));
                        break;
                    }
                }
            } catch (final Exception e) {
                log.log(Level.WARNING, "TestException", e);
                if (!exceptionExpected) {
                    fail(String.format("Unexpected Exception thrown (%s)! in TestCommand: %s, %s. %s",
                            e, testCase.getTestCommand(), testCase.getMetadata().getDetailedIdentifier(), testCase.getComment()));
                }
            }
        }
        assertAll(assertions);
    }

    public void runCommand(final TestCase<?> testCase) throws Exception {
        fail(new NoSuchMethodError(String.format(
                "No implementation found for the command \"%1$s\". Please override this method in your Unit-Test " +
                        "or provide a method or class annotated with @TestCommand(\"%1$s\")",
                testCase.getTestCommand())));
    }

    @BeforeAll
    public static void initializeTest() throws Exception {
        final String clazzName = JExUnitConfig.getStringProperty(JExUnitConfig.ConfigKey.BEFORE_EXCEL);
        if (clazzName != null && !clazzName.isEmpty()) {
            final Class<?> name = Class.forName(clazzName);
            if (BeforeSheet.class.isAssignableFrom(name)) {
                final BeforeSheet instance = (BeforeSheet) name.getDeclaredConstructor().newInstance();
                instance.run();
            }
        }
    }

    @AfterAll
    public static void finalizeTest() throws Exception {
        final String clazzName = JExUnitConfig.getStringProperty(JExUnitConfig.ConfigKey.AFTER_EXCEL);
        if (clazzName != null && !clazzName.isEmpty()) {
            final Class<?> name = Class.forName(clazzName);
            if (AfterSheet.class.isAssignableFrom(name)) {
                final AfterSheet instance = (AfterSheet) name.getDeclaredConstructor().newInstance();
                instance.run();
            }
        }
    }
}
