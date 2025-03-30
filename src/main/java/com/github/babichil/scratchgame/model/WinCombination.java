package com.github.babichil.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@With
public class WinCombination {
    @JsonProperty("reward_multiplier")
    double rewardMultiplier;
    String when;
    int count;
    String group;

    @JsonProperty("covered_areas")
    List<List<String>> coveredAreas;

    @JsonCreator
    public WinCombination(
            @JsonProperty("reward_multiplier") double rewardMultiplier,
            @JsonProperty("when") String when,
            @JsonProperty("count") int count,
            @JsonProperty("group") String group,
            @JsonProperty("covered_areas") List<List<String>> coveredAreas
    ) {
        this.rewardMultiplier = rewardMultiplier;
        this.when = when;
        this.count = count;
        this.group = group;
        this.coveredAreas = coveredAreas;
    }
}
