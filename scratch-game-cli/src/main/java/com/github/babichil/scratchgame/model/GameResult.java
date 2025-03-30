package com.github.babichil.scratchgame.model;

import lombok.With;

import java.util.List;
import java.util.Map;

@With
public record GameResult(String[][] matrix, double reward, List<String> appliedBonusSymbols,
                         Map<String, List<String>> appliedWinningCombinations) {
}
