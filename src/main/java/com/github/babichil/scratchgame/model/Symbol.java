package com.github.babichil.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Symbol {
    @JsonProperty("reward_multiplier")
    public Double rewardMultiplier;
    public String type;
    public Integer extra;
    public String impact;
}
