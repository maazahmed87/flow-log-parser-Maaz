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

public class TagCountOutputGeneratorTest {
    private static final String TEST_OUTPUT_PATH = "test_tag_count_output.csv";
    private TagCountOutputGenerator outputGenerator;
    private Map<String, Integer> testTagCounts;

    @Before
    public void setUp() {
        testTagCounts = new HashMap<>();
        testTagCounts.put("http", 100);
        testTagCounts.put("https", 200);
        outputGenerator = new TagCountOutputGenerator(TEST_OUTPUT_PATH, testTagCounts);
    }

    @Test
    public void testGenerateOutput() throws IOException {
        outputGenerator.generateOutput();

        File outputFile = new File(TEST_OUTPUT_PATH);
        assertTrue(outputFile.exists());

        String content = new String(Files.readAllBytes(Paths.get(TEST_OUTPUT_PATH)));
        assertTrue(content.contains("Tag,Count"));
        assertTrue(content.contains("http,100"));
        assertTrue(content.contains("https,200"));
    }

    // Add more tests as needed
}