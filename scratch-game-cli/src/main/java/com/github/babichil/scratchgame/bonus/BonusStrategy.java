package com.github.babichil.scratchgame.bonus;

import com.github.babichil.scratchgame.model.GameResult;
import com.github.babichil.scratchgame.model.Symbol;

public interface BonusStrategy {
    GameResult apply(GameResult result, Symbol symbol);
}
