package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.GameResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameEngineTest {

    private IMatrixGenerator matrixGenerator;
    private IRewardCalculator rewardCalculator;
    private IBonusApplier bonusApplier;
    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        matrixGenerator = mock(IMatrixGenerator.class);
        rewardCalculator = mock(IRewardCalculator.class);
        bonusApplier = mock(IBonusApplier.class);
        gameEngine = new GameEngine(matrixGenerator, rewardCalculator, bonusApplier);
    }

    @Test
    void play_shouldReturnFinalResult_whenAllComponentsWork() {
        String[][] matrix = {{"A", "B"}, {"C", "D"}};
        GameResult calculated = new GameResult(null, 100.0, List.of(), Map.of());
        GameResult withBonus = calculated.withReward(150.0).withAppliedBonusSymbols(List.of("+500"));

        when(matrixGenerator.generate()).thenReturn(matrix);
        when(rewardCalculator.calculate(matrix, 100)).thenReturn(calculated);
        when(bonusApplier.applyBonus(matrix, calculated)).thenReturn(withBonus);

        GameResult result = gameEngine.play(100);

        assertEquals(150.0, result.reward());
        assertEquals(List.of("+500"), result.appliedBonusSymbols());
        assertArrayEquals(matrix, result.matrix());
    }

    @Test
    void play_shouldHandleEmptyMatrix() {
        String[][] matrix = new String[0][0];
        GameResult initial = new GameResult(null, 0.0, List.of(), Map.of());

        when(matrixGenerator.generate()).thenReturn(matrix);
        when(rewardCalculator.calculate(matrix, 50)).thenReturn(initial);
        when(bonusApplier.applyBonus(matrix, initial)).thenReturn(initial);

        GameResult result = gameEngine.play(50);

        assertEquals(0.0, result.reward());
        assertEquals(0, result.matrix().length);
    }

    @Test
    void play_shouldNotApplyBonus_whenRewardIsZero() {
        String[][] matrix = {{"F"}};
        GameResult initial = new GameResult(null, 0.0, List.of(), Map.of());

        when(matrixGenerator.generate()).thenReturn(matrix);
        when(rewardCalculator.calculate(matrix, 75)).thenReturn(initial);
        when(bonusApplier.applyBonus(matrix, initial)).thenReturn(initial);

        GameResult result = gameEngine.play(75);

        assertEquals(0.0, result.reward());
        assertTrue(result.appliedBonusSymbols().isEmpty());
    }

    @Test
    void play_shouldApplyMultipleBonusesCorrectly() {
        String[][] matrix = {{"10x"}};
        GameResult initial = new GameResult(null, 100.0, List.of(), Map.of());
        GameResult afterBonus = initial.withReward(1000.0).withAppliedBonusSymbols(List.of("10x"));

        when(matrixGenerator.generate()).thenReturn(matrix);
        when(rewardCalculator.calculate(matrix, 100)).thenReturn(initial);
        when(bonusApplier.applyBonus(matrix, initial)).thenReturn(afterBonus);

        GameResult result = gameEngine.play(100);

        assertEquals(1000.0, result.reward());
        assertEquals(List.of("10x"), result.appliedBonusSymbols());
    }

    @Test
    void play_shouldReplaceMatrixInResult() {
        String[][] matrix = {{"E"}};
        GameResult initial = new GameResult(null, 30.0, List.of(), Map.of());
        GameResult bonusApplied = initial.withReward(50.0).withAppliedBonusSymbols(List.of("+500"));

        when(matrixGenerator.generate()).thenReturn(matrix);
        when(rewardCalculator.calculate(matrix, 10)).thenReturn(initial);
        when(bonusApplier.applyBonus(matrix, initial)).thenReturn(bonusApplied);

        GameResult result = gameEngine.play(10);

        assertArrayEquals(matrix, result.matrix());
    }
}
