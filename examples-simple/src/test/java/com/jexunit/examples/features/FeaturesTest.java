package com.jexunit.examples.features;

import com.jexunit.core.JExUnitBase;
import com.jexunit.core.commands.annotation.TestCommand;
import com.jexunit.core.model.TestCase;
import com.jexunit.examples.arithmeticaltests.ArithmeticalTest;
import com.jexunit.examples.arithmeticaltests.ArithmeticalTestCommands;
import com.jexunit.examples.arithmeticaltests.model.ArithmeticalTestObject;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Disabled("How to test the fast fail feature?")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeaturesTest {

	static int executionsOfMulcommand = 0;

	@TestFactory
	@Order(1)
	Object test() {
		return JExUnitBase.builder()
				.worksheetAsTest(true)
				.testType(this.getClass())
				.testType(ArithmeticalTest.class)
				.testType(ArithmeticalTestCommands.class)
				.path("src/test/resources/FeaturesTest.xlsx").build().register();
	}

	@Test
	@Order(2)
	void testExecutionsOfMulcommand() {
		// That test needs to be green to manually test the fast fail command
		assertThat(executionsOfMulcommand).isEqualTo(1);
	}

	@TestCommand("mul")
	public void runMulCommand(final TestCase<?> testCase, final ArithmeticalTestObject testObject) {
		executionsOfMulcommand++;
		assertThat(testObject.getParam1() * testObject.getParam2()).isEqualTo(testObject.getResult());
	}


}
