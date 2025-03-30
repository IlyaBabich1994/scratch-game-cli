package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.GameResult;

public interface IRewardCalculator {
    GameResult calculate(String[][] matrix, int betAmount);
}
