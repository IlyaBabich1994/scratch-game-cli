package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.GameConfig;
import com.github.babichil.scratchgame.model.GameResult;
import com.github.babichil.scratchgame.model.Symbol;
import com.github.babichil.scratchgame.model.SymbolType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BonusApplierTest {
    @Test
    void shouldSkipBonusIfRewardIsZero() {
        GameConfig config = new GameConfig(3, 3, Map.of(), null, Map.of());
        BonusApplier applier = new BonusApplier(config);
        GameResult result = new GameResult(new String[3][3], 0, List.of(), Map.of());

        GameResult updated = applier.applyBonus(new String[3][3], result);

        assertEquals(0, updated.reward());
        assertTrue(updated.appliedBonusSymbols().isEmpty());
    }

    @Test
    void shouldApplyMultiplyRewardBonus() {
        Symbol bonus = new Symbol(10.0, SymbolType.BONUS.name(), null, "multiply_reward");
        Map<String, Symbol> symbols = Map.of("10x", bonus);
        String[][] matrix = {{"10x"}};
        GameResult initial = new GameResult(matrix, 100.0, List.of(), Map.of());

        GameConfig config = new GameConfig(1, 1, symbols, null, Map.of());
        BonusApplier applier = new BonusApplier(config);

        GameResult updated = applier.applyBonus(matrix, initial);

        assertEquals(1000.0, updated.reward());
        assertEquals(List.of("10x"), updated.appliedBonusSymbols());
    }

    @Test
    void shouldApplyExtraBonus() {
        Symbol bonus = new Symbol(null, SymbolType.BONUS.name(), 500.0, "extra_bonus");
        Map<String, Symbol> symbols = Map.of("+500", bonus);
        String[][] matrix = {{"+500"}};
        GameResult initial = new GameResult(matrix, 100.0, List.of(), Map.of());

        GameConfig config = new GameConfig(1, 1, symbols, null, Map.of());
        BonusApplier applier = new BonusApplier(config);

        GameResult updated = applier.applyBonus(matrix, initial);

        assertEquals(600.0, updated.reward());
        assertEquals(List.of("+500"), updated.appliedBonusSymbols());
    }

    @Test
    void shouldApplyMultipleBonuses() {
        Map<String, Symbol> symbols = Map.of(
                "10x", new Symbol(10.0, SymbolType.BONUS.name(), null, "multiply_reward"),
                "+500", new Symbol(null, SymbolType.BONUS.name(), 500.0, "extra_bonus")
        );
        String[][] matrix = {
                {"10x", "+500"},
                {null, null}
        };
        GameResult initial = new GameResult(matrix, 100.0, List.of(), Map.of());

        GameConfig config = new GameConfig(2, 2, symbols, null, Map.of());
        BonusApplier applier = new BonusApplier(config);

        GameResult updated = applier.applyBonus(matrix, initial);

        assertEquals(1500.0, updated.reward());
        assertTrue(updated.appliedBonusSymbols().contains("10x"));
        assertTrue(updated.appliedBonusSymbols().contains("+500"));
    }

    @Test
    void shouldIgnoreUnknownSymbols() {
        String[][] matrix = {{"UNKNOWN"}};
        GameConfig config = new GameConfig(1, 1, Map.of(), null, Map.of());
        GameResult initial = new GameResult(matrix, 100.0, List.of(), Map.of());

        BonusApplier applier = new BonusApplier(config);
        GameResult updated = applier.applyBonus(matrix, initial);

        assertEquals(100.0, updated.reward());
        assertTrue(updated.appliedBonusSymbols().isEmpty());
    }

    @Test
    void shouldApplyMissBonusWithoutEffect() {
        Symbol miss = new Symbol(null, SymbolType.BONUS.name(), null, "miss");
        GameConfig config = new GameConfig(1, 1, Map.of("MISS", miss), null, Map.of());
        String[][] matrix = {{"MISS"}};
        GameResult initial = new GameResult(matrix, 100.0, List.of(), Map.of());

        BonusApplier applier = new BonusApplier(config);
        GameResult updated = applier.applyBonus(matrix, initial);

        assertEquals(100.0, updated.reward());
        assertEquals(List.of("MISS"), updated.appliedBonusSymbols());
    }
}