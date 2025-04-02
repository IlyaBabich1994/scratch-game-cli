package com.github.babichil.scratchgame.model;

import lombok.Getter;

@Getter
public enum BonusImpact {
    MULTIPLY_REWARD("multiply_reward"),
    EXTRA_BONUS("extra_bonus"),
    MISS("miss");

    private final String value;

    BonusImpact(String value) {
        this.value = value;
    }

    public static BonusImpact fromString(String impact) {
        for (BonusImpact value : values()) {
            if (value.name().equalsIgnoreCase(impact)) {
                return value;
            }
        }
        return MISS;
    }
}
