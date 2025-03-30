package com.github.babichil.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class StandardSymbolProbability {
    private int column;
    private int row;
    private Map<String, Integer> symbols;


    @JsonCreator
    public StandardSymbolProbability(
            @JsonProperty("column") int column,
            @JsonProperty("row") int row,
            @JsonProperty("symbols") Map<String, Integer> symbols
    ) {
        this.column = column;
        this.row = row;
        this.symbols = symbols;
    }
}
