package com.github.babichil.scratchgame.model;

import lombok.Getter;

@Getter
public enum SymbolType {
    STANDARD("standard"),
    BONUS("bonus");

    private final String value;

    SymbolType(String value) {
        this.value = value;
    }

    public static SymbolType fromString(String value) {
        for (SymbolType type : SymbolType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SymbolType: " + value);
    }
}