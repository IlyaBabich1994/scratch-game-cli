package com.github.babichil.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Symbol {
    @JsonProperty("reward_multiplier")
    Double rewardMultiplier;
    SymbolType type;
    Double extra;
    String impact;

    @JsonCreator
    public Symbol(
            @JsonProperty("reward_multiplier") Double rewardMultiplier,
            @JsonProperty("type") String type,
            @JsonProperty("extra") Double extra,
            @JsonProperty("impact") String impact
    ) {
        this.rewardMultiplier = rewardMultiplier;
        this.type = SymbolType.fromString(type);
        this.extra = extra;
        this.impact = impact;
    }

    public BonusImpact getImpactEnum() {
        return BonusImpact.fromString(impact);
    }
}

