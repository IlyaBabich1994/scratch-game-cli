package com.github.babichil.scratchgame.bonus;

import com.github.babichil.scratchgame.model.GameResult;
import com.github.babichil.scratchgame.model.Symbol;

public class MultiplyRewardStrategy implements BonusStrategy {
    @Override
    public GameResult apply(GameResult result, Symbol symbol) {
        return result.withReward(result.reward() * symbol.getRewardMultiplier());
    }
}
