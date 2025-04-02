package com.github.babichil.scratchgame.config;

import com.github.babichil.scratchgame.model.GameConfig;

public interface ConfigLoader {
    GameConfig loadConfig(String source);
}
