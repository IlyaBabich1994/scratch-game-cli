package com.github.babichil.scratchgame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.babichil.scratchgame.model.GameConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileConfigLoader implements ConfigLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GameConfig loadConfig(String source) {
        try {
            InputStream is = tryLoadFromFileSystem(source);
            if (is == null) {
                is = tryLoadFromClasspath(source);
            }

            if (is == null) {
                throw new FileNotFoundException("Configuration file not found in filesystem or classpath: " + source);
            }

            GameConfig config = objectMapper.readValue(is, GameConfig.class);
            log.info("Successfully loaded configuration from '{}'", source);
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config from " + source, e);
        }
    }

    private InputStream tryLoadFromFileSystem(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                log.debug("Loading config from filesystem: {}", path);
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                log.warn("File not found on filesystem: {}", path);
            }
        }
        return null;
    }

    private InputStream tryLoadFromClasspath(String path) {
        log.debug("Trying to load config from classpath: {}", path);
        return getClass().getClassLoader().getResourceAsStream(path);
    }
}

