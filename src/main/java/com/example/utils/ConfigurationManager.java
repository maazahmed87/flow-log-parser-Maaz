package com.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {
    private Properties properties = new Properties();

    // Constructor to load properties from a configuration file
    public ConfigurationManager(String configFilePath) {
        try (InputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("ERROR: Error loading configuration file: " + ex.getMessage());
        }
    }

    // Get a property value, or return a default if not found
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
