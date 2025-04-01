package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MatrixGeneratorTest {

    private GameConfig config;
    private IMatrixGenerator generator;
    private Set<String> knownSymbols;

    @BeforeEach
    void setup() {
        Map<String, Symbol> symbols = new HashMap<>();
        symbols.put("A", new Symbol(5.0, SymbolType.STANDARD.name(), null, null));
        symbols.put("B", new Symbol(3.0, SymbolType.STANDARD.name(), null, null));
        symbols.put("C", new Symbol(2.0, SymbolType.STANDARD.name(), null, null));
        symbols.put("10x", new Symbol(10.0, SymbolType.BONUS.name(), null, "multiply_reward"));
        symbols.put("+1000", new Symbol(null, SymbolType.BONUS.name(), 1000.0, "extra_bonus"));
        symbols.put("MISS", new Symbol(null, SymbolType.BONUS.name(), null, "miss"));

        List<StandardSymbolProbability> standardProbs = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Map<String, Integer> prob = Map.of("A", 1, "B", 2, "C", 3);
                standardProbs.add(new StandardSymbolProbability(c, r, prob));
            }
        }

        BonusSymbolProbability bonusProb = new BonusSymbolProbability(
                Map.of("10x", 1, "+1000", 2, "MISS", 3)
        );

        Probabilities probabilities = new Probabilities(standardProbs, bonusProb);
        config = new GameConfig(3, 3, symbols, probabilities, Map.of());
        generator = new MatrixGenerator(config);

        knownSymbols = symbols.keySet();
    }

    @Test
    void testMatrixSizeIsCorrect() {
        String[][] matrix = generator.generate();
        assertEquals(3, matrix.length);
        assertEquals(3, matrix[0].length);
    }

    @Test
    void testAllCellsAreFilled() {
        String[][] matrix = generator.generate();
        for (String[] row : matrix) {
            for (String cell : row) {
                assertNotNull(cell, "Cell should not be null");
            }
        }
    }

    @Test
    void testAllSymbolsAreFromConfig() {
        String[][] matrix = generator.generate();
        for (String[] row : matrix) {
            for (String cell : row) {
                assertTrue(knownSymbols.contains(cell), "Unknown symbol found: " + cell);
            }
        }
    }

    @RepeatedTest(20)
    void testBonusSymbolCountDoesNotExceedMax() {
        String[][] matrix = generator.generate();
        long bonusCount = Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(s -> config.symbols().get(s).isBonus())
                .count();

        int maxBonus = (int) Math.ceil(3 * 3 * 0.25);
        assertTrue(bonusCount <= maxBonus, "Too many bonus symbols: " + bonusCount);
    }

    @RepeatedTest(10)
    void testFallbackDoesNotAppearWhenConfigIsCorrect() {
        String[][] matrix = generator.generate();
        for (String[] row : matrix) {
            for (String cell : row) {
                assertNotEquals("F", cell, "Fallback symbol appeared");
            }
        }
    }
}
