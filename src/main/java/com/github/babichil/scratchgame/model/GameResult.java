package com.github.babichil.scratchgame.model;

import lombok.Value;
import lombok.With;

import java.util.List;
import java.util.Map;

@Value
@With
public class GameResult {
    String[][] matrix;
    double reward;
    List<String> appliedBonusSymbols;
    Map<String, List<String>> appliedWinningCombinations;
}
