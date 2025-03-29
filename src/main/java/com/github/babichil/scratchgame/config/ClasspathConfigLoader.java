package com.github.babichil.scratchgame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.babichil.scratchgame.model.GameConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ClasspathConfigLoader implements ConfigLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GameConfig loadConfig(String source) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(source)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + source);
            }
            GameConfig config = objectMapper.readValue(is, GameConfig.class);
            log.info("Successfully loaded configuration from '{}'", source);
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config from " + source, e);
        }
    }
}

