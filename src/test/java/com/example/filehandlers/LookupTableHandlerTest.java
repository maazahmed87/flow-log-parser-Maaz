package com.example.filehandlers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class LookupTableHandlerTest {
    private static final String TEST_LOOKUP_PATH = "test_lookup.csv";
    private LookupTableHandler lookupHandler;

    @Before
    public void setUp() throws IOException {
        // Create a temporary lookup file for testing
        File lookupFile = new File(TEST_LOOKUP_PATH);
        try (FileWriter writer = new FileWriter(lookupFile)) {
            writer.write("port,protocol,tag\n");
            writer.write("80,tcp,http\n");
            writer.write("443,tcp,https\n");
        }
        lookupHandler = new LookupTableHandler(TEST_LOOKUP_PATH);
    }

    @Test
    public void testProcessFile() throws IOException {
        lookupHandler.processFile();
        Map<String, String> lookup = lookupHandler.getLookup();

        assertEquals(2, lookup.size());
        assertEquals("http", lookup.get("80,tcp"));
        assertEquals("https", lookup.get("443,tcp"));
    }

    @Test
    public void testIsFileValid() {
        assertTrue(lookupHandler.isFileValid());
    }

    // Add more tests as needed
}
