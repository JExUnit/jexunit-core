package com.jexunit.core;

import com.jexunit.core.commands.annotation.TestCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

public class SimpleTest {

    @TestFactory
    public Stream<DynamicNode> test() {
        return JExUnitBase.builder().paths(List.of("src/test/resources/sum-and-multiply.xlsx"
                , "src/test/resources/sum-and-multiply-2.xlsx")).build().register();
    }

    @TestCommand(value = "multiply")
    public static void runMulCommand(SumObj obj) throws Exception {
        Assertions.assertThat(obj.val1 * obj.val2).isEqualTo(obj.result);
    }

    @TestCommand(value = "sum")
    public static void sumCommand(SumObj obj) throws Exception {
        Assertions.assertThat(obj.val1 + obj.val2).isEqualTo(obj.result);
    }

}
