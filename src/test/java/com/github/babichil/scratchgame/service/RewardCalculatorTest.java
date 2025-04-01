package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RewardCalculatorTest {

    private static final int BET = 100;
    private GameConfig config;

    @BeforeEach
    void setup() {
        config = mock(GameConfig.class);
    }

    static Stream<TestCase> provideMatrixAndExpectedReward() {
        return Stream.of(
                new TestCase(
                        new String[][]{
                                {"A", "A", "A"},
                                {"B", "C", "D"},
                                {"E", "F", "C"}
                        },
                        BET * 5.0 * 1.0,
                        Map.of("A", List.of("same_symbol_3_times"))
                ),
                new TestCase(
                        new String[][]{
                                {"A", "A", "A"},
                                {"A", "A", "A"},
                                {"A", "A", "A"}
                        },
                        BET * 5.0 * 20.0,
                        Map.of("A", List.of("same_symbol_9_times"))
                ),
                new TestCase(
                        new String[][]{
                                {"B", "B", "B"},
                                {"B", "B", "B"},
                                {"B", "B", "B"}
                        },
                        BET * 3.0 * 20.0,
                        Map.of("B", List.of("same_symbol_9_times"))
                ),
                new TestCase(
                        new String[][]{
                                {"F", "F", "F"},
                                {"F", "F", "F"},
                                {"F", "F", "F"}
                        },
                        BET * 1.0 * 20.0,
                        Map.of("F", List.of("same_symbol_9_times"))
                ),
                new TestCase(
                        new String[][]{
                                {"A", "B", "C"},
                                {"D", "E", "F"},
                                {"F", "E", "D"}
                        },
                        0.0,
                        Map.of()
                )
        );
    }

    @ParameterizedTest(name = "{index} => matrix={0}")
    @MethodSource("provideMatrixAndExpectedReward")
    @DisplayName("Should calculate reward correctly for various matrices")
    void shouldCalculateRewardCorrectly(TestCase testCase) {
        Map<String, Symbol> symbols = Map.of(
                "A", new Symbol(5.0, SymbolType.STANDARD.name(), null, null),
                "B", new Symbol(3.0, SymbolType.STANDARD.name(), null, null),
                "C", new Symbol(2.5, SymbolType.STANDARD.name(), null, null),
                "D", new Symbol(2.0, SymbolType.STANDARD.name(), null, null),
                "E", new Symbol(1.2, SymbolType.STANDARD.name(), null, null),
                "F", new Symbol(1.0, SymbolType.STANDARD.name(), null, null)
        );

        Map<String, WinCombination> combinations = new HashMap<>();
        for (int i = 3; i <= 9; i++) {
            double rewardMultiplier = switch (i) {
                case 3 -> 1.0;
                case 4 -> 1.5;
                case 5 -> 2.0;
                case 6 -> 3.0;
                case 7 -> 5.0;
                case 8 -> 10.0;
                case 9 -> 20.0;
                default -> 1.0;
            };
            combinations.put("same_symbol_" + i + "_times",
                    new WinCombination(rewardMultiplier, "same_symbols", i, "same_symbols", null));
        }

        when(config.symbols()).thenReturn(symbols);
        when(config.winCombinations()).thenReturn(combinations);
        when(config.probabilities()).thenReturn(new Probabilities(List.of(), null));

        RewardCalculator calculator = new RewardCalculator(config);
        GameResult result = calculator.calculate(testCase.matrix(), BET);

        assertEquals(testCase.expectedReward(), result.reward(), 0.001);
        assertEquals(testCase.expectedCombinations(), result.appliedWinningCombinations());
    }

    @ParameterizedTest
    @MethodSource("horizontalCombinationsProvider")
    void shouldApplyHorizontalLineCombinationParameterized(String[][] matrix, double expectedReward) {
        GameConfig config = new GameConfig(
                3, 3,
                Map.of("A", standard()),
                new Probabilities(List.of(), null),
                Map.of(
                        "horizontal", new WinCombination(
                                3.0, "linear_symbols", 0, "group1",
                                List.of(List.of("0:0", "0:1", "0:2"))
                        )
                )
        );

        RewardCalculator calc = new RewardCalculator(config);
        GameResult result = calc.calculate(matrix, 100);

        assertEquals(expectedReward, result.reward(), 0.001);
    }

    private static Stream<Arguments> horizontalCombinationsProvider() {
        return Stream.of(
                Arguments.of(new String[][] {
                        {"A", "A", "A"},
                        {"B", "C", "D"},
                        {"E", "F", "G"},
                }, 100 * 2 * 3),
                Arguments.of(new String[][] {
                        {"A", "X", "A"},
                        {"A", "A", "A"},
                        {"E", "F", "G"},
                }, 0.0)
        );
    }


    private static Symbol standard() {
        return new Symbol(2.0, SymbolType.STANDARD.name(), null, null);
    }

    record TestCase(String[][] matrix, double expectedReward, Map<String, List<String>> expectedCombinations) {}
}
