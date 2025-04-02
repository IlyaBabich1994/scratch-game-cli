package com.github.babichil.scratchgame.bonus;

import com.github.babichil.scratchgame.model.GameResult;
import com.github.babichil.scratchgame.model.Symbol;
import com.github.babichil.scratchgame.model.SymbolType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BonusStrategyFactoryTest {

    @Test
    void shouldReturnMultiplyRewardStrategy() {
        BonusStrategy strategy = BonusStrategyFactory.getStrategy("multiply_reward");
        Symbol symbol = new Symbol(2.0, SymbolType.BONUS.name(), null, "multiply_reward");
        GameResult result = new GameResult(new String[0][0], 100.0, List.of(), Map.of());

        GameResult updated = strategy.apply(result, symbol);

        assertEquals(200.0, updated.reward());
    }

    @Test
    void shouldReturnExtraBonusStrategy() {
        BonusStrategy strategy = BonusStrategyFactory.getStrategy("extra_bonus");
        Symbol symbol = new Symbol(null, SymbolType.BONUS.name(), 500.0, "extra_bonus");
        GameResult result = new GameResult(new String[0][0], 100.0, List.of(), Map.of());

        GameResult updated = strategy.apply(result, symbol);

        assertEquals(600.0, updated.reward());
    }

    @Test
    void shouldReturnMissStrategy() {
        BonusStrategy strategy = BonusStrategyFactory.getStrategy("miss");
        Symbol symbol = new Symbol(null, SymbolType.BONUS.name(), null, "miss");
        GameResult result = new GameResult(new String[0][0], 100.0, List.of(), Map.of());

        GameResult updated = strategy.apply(result, symbol);

        assertEquals(100.0, updated.reward());
    }

    @Test
    void shouldFallbackToMissStrategyOnUnknownImpact() {
        BonusStrategy strategy = BonusStrategyFactory.getStrategy("unknown_impact");
        Symbol symbol = new Symbol(10.0, SymbolType.BONUS.name(), 1000.0, "unknown_impact");
        GameResult result = new GameResult(new String[0][0], 123.0, List.of(), Map.of());

        GameResult updated = strategy.apply(result, symbol);

        assertEquals(123.0, updated.reward(), "Unknown impact should fallback to MissBonusStrategy");
    }
}
