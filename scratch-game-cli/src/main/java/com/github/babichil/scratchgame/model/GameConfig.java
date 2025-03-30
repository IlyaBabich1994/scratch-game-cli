package com.github.babichil.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.util.Collections;
import java.util.Map;


@With
public record GameConfig(int columns, int rows, Map<String, Symbol> symbols, Probabilities probabilities,
                         @JsonProperty("win_combinations") Map<String, WinCombination> winCombinations) {
    @JsonCreator
    public GameConfig(
            @JsonProperty("columns") int columns,
            @JsonProperty("rows") int rows,
            @JsonProperty("symbols") Map<String, Symbol> symbols,
            @JsonProperty("probabilities") Probabilities probabilities,
            @JsonProperty("win_combinations") Map<String, WinCombination> winCombinations
    ) {
        this.columns = columns;
        this.rows = rows;
        this.symbols = symbols != null ? Collections.unmodifiableMap(symbols) : Collections.emptyMap();
        this.probabilities = probabilities;
        this.winCombinations = winCombinations != null ? Collections.unmodifiableMap(winCombinations) : Collections.emptyMap();
    }
}
