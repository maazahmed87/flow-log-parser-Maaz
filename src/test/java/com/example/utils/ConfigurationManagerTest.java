package com.example.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigurationManagerTest {
    private static final String TEST_CONFIG_PATH = "test_config.properties";
    private ConfigurationManager configManager;

    @Before
    public void setUp() throws IOException {
        File configFile = new File(TEST_CONFIG_PATH);
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("test.key=test_value\n");
            writer.write("another.key=another_value\n");
        }
        configManager = new ConfigurationManager(TEST_CONFIG_PATH);
    }

    @Test
    public void testGetExistingProperty() {
        assertEquals("test_value", configManager.getProperty("test.key", "default"));
    }

    @Test
    public void testGetNonExistingProperty() {
        assertEquals("default", configManager.getProperty("non.existing.key", "default"));
    }
}
