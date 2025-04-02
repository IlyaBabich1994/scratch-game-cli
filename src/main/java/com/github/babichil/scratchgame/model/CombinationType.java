package com.github.babichil.scratchgame.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CombinationType {
    SAME_SYMBOLS("same_symbols"),
    LINEAR_SYMBOLS("linear_symbols");

    private final String jsonValue;

    CombinationType(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public static CombinationType from(String value) {
        return Arrays.stream(values())
                .filter(t -> t.jsonValue.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown combination type: " + value));
    }
}

