package com.github.babichil.scratchgame.bonus;


import com.github.babichil.scratchgame.model.BonusImpact;

import java.util.EnumMap;
import java.util.Map;

public final class BonusStrategyFactory {

    private static final Map<BonusImpact, BonusStrategy> STRATEGIES = new EnumMap<>(BonusImpact.class);

    static {
        STRATEGIES.put(BonusImpact.MULTIPLY_REWARD, new MultiplyRewardStrategy());
        STRATEGIES.put(BonusImpact.EXTRA_BONUS, new ExtraBonusStrategy());
        STRATEGIES.put(BonusImpact.MISS, new MissBonusStrategy());
    }

    private BonusStrategyFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static BonusStrategy getStrategy(String impact) {
        BonusImpact type = BonusImpact.fromString(impact);
        return STRATEGIES.getOrDefault(type, new MissBonusStrategy());
    }
}
