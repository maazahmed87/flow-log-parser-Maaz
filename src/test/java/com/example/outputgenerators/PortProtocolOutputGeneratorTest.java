package com.example.outputgenerators;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PortProtocolOutputGeneratorTest {
    private static final String TEST_OUTPUT_PATH = "test_port_protocol_output.csv";
    private PortProtocolOutputGenerator outputGenerator;
    private Map<String, Integer> testPortProtocolCounts;

    @Before
    public void setUp() {
        testPortProtocolCounts = new HashMap<>();
        testPortProtocolCounts.put("80,tcp", 150);
        testPortProtocolCounts.put("443,tcp", 250);
        outputGenerator = new PortProtocolOutputGenerator(TEST_OUTPUT_PATH, testPortProtocolCounts);
    }

    @Test
    public void testGenerateOutput() throws IOException {
        outputGenerator.generateOutput();

        File outputFile = new File(TEST_OUTPUT_PATH);
        assertTrue(outputFile.exists());

        String content = new String(Files.readAllBytes(Paths.get(TEST_OUTPUT_PATH)));
        assertTrue(content.contains("Port/Protocol,Count"));
        assertTrue(content.contains("80,tcp,150"));
        assertTrue(content.contains("443,tcp,250"));
    }

    // Add more tests as needed
}