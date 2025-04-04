package com.github.babichil.scratchgame.config;

import com.github.babichil.scratchgame.model.GameConfig;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class FileConfigLoaderTest {
    private final ConfigLoader loader = new FileConfigLoader();

    @Test
    void shouldLoadConfigSuccessfully() {
        GameConfig config = loader.loadConfig("test-config.json");

        assertNotNull(config);
        assertEquals(3, config.columns());
        assertEquals(3, config.rows());
        assertTrue(config.symbols().containsKey("A"));
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        Exception exception = assertThrows(RuntimeException.class, () -> loader.loadConfig("missing-file.json"));
        assertInstanceOf(FileNotFoundException.class, exception.getCause());
    }

    @Test
    void shouldThrowExceptionWhenInvalidJson() {
        Exception exception = assertThrows(RuntimeException.class, () -> loader.loadConfig("invalid-config.json"));
        assertTrue(exception.getMessage().startsWith("Failed to load config from"));
    }

}
