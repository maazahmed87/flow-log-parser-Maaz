package com.example.filehandlers;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FlowLogHandlerTest {
    private static final String TEST_FLOWLOG_PATH = "test_flowlog.txt";
    private FlowLogHandler flowLogHandler;
    private Map<String, String> testLookup;

    @Before
    public void setUp() throws IOException {
        
        File flowLogFile = new File(TEST_FLOWLOG_PATH);
        try (FileWriter writer = new FileWriter(flowLogFile)) {
            writer.write("2 1 3 10.0.0.1 10.0.0.2 80 443 6 1000 2000 10 20 0 0\n");
            writer.write("2 1 3 10.0.0.3 10.0.0.4 22 80 17 1000 2000 10 20 0 0\n");
        }

        testLookup = new HashMap<>();
        testLookup.put("443,tcp", "sv_p1");
        testLookup.put("80,udp", "sv_p2");

        flowLogHandler = new FlowLogHandler(TEST_FLOWLOG_PATH, testLookup);
    }

    @Test
    public void testProcessFile() throws IOException {
        flowLogHandler.processFile();

        Map<String, Integer> tagCounts = flowLogHandler.getTagCounts();
        Map<String, Integer> portProtocolCounts = flowLogHandler.getPortProtocolCounts();

        // Debug print statements
        System.out.println("Tag Counts: " + tagCounts);
        System.out.println("Port Protocol Counts: " + portProtocolCounts);

        assertNotNull("Tag counts should not be null", tagCounts);
        assertNotNull("Port protocol counts should not be null", portProtocolCounts);

        assertEquals("Expected 2 unique tags", 2, tagCounts.size());
        assertEquals("Expected 1 count for 'sv_p1' tag", Integer.valueOf(1), tagCounts.get("sv_p1"));
        assertEquals("Expected 1 count for 'sv_p2' tag", Integer.valueOf(1), tagCounts.get("sv_p2"));

        assertEquals("Expected 2 unique port/protocol combinations", 2, portProtocolCounts.size());
        assertEquals("Expected 1 count for '443,tcp'", Integer.valueOf(1), portProtocolCounts.get("443,tcp"));
        assertEquals("Expected 1 count for '80,udp'", Integer.valueOf(1), portProtocolCounts.get("80,udp"));
    }

    @After
    public void tearDown() {
        new File(TEST_FLOWLOG_PATH).delete();
    }
}