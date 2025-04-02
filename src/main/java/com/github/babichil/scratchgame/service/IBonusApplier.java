package com.github.babichil.scratchgame.service;

import com.github.babichil.scratchgame.model.GameResult;

public interface IBonusApplier {
    GameResult applyBonus(String[][] matrix, GameResult result);
}
