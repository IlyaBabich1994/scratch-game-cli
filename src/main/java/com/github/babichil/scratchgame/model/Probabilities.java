package com.github.babichil.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class Probabilities {
    @JsonProperty("standard_symbols")
    List<StandardSymbolProbability> standardSymbols;

    @JsonProperty("bonus_symbols")
    BonusSymbolProbability bonusSymbols;

    @JsonCreator
    public Probabilities(
            @JsonProperty("standard_symbols") List<StandardSymbolProbability> standardSymbols,
            @JsonProperty("bonus_symbols") BonusSymbolProbability bonusSymbols
    ) {
        this.standardSymbols = standardSymbols;
        this.bonusSymbols = bonusSymbols;
    }
}
