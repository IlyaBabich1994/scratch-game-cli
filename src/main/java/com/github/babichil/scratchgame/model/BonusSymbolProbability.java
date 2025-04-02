package com.github.babichil.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.util.Map;

@With
public record BonusSymbolProbability(Map<String, Integer> symbols) {
    @JsonCreator
    public BonusSymbolProbability(@JsonProperty("symbols") Map<String, Integer> symbols) {
        this.symbols = symbols;
    }
}
