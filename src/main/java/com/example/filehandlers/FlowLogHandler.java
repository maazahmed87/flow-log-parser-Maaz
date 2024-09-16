package com.example.filehandlers;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FlowLogHandler extends FileHandler {
    private Map<String, String> lookup;
    private Map<String, Integer> tagCounts;
    private Map<String, Integer> portProtocolCounts;

    public FlowLogHandler(String filePath, Map<String, String> lookup) {
        super(filePath);
        this.lookup = lookup;
        this.tagCounts = new ConcurrentHashMap<>();
        this.portProtocolCounts = new ConcurrentHashMap<>();
    }

    @Override
    public void processFile() throws IOException {
        processFile(null);
    }

    // Process the flow log file, optionally using parallel execution
    public void processFile(ExecutorService executor) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            List<Future<?>> futures = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                final String logLine = line;
                if (executor != null) {
                    futures.add(executor.submit(() -> processLine(logLine)));
                } else {
                    processLine(logLine);
                }
            }

            if (executor != null) {
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new IOException("ERROR: Can't process lines in parallel", e);
                    }
                }
            }
        }
    }

    // Process a single line of the flow log file
    private void processLine(String line) {
        String[] fields = line.trim().split("\\s+");

        if (fields.length != 14) {
            System.err.println("ERROR: Malformed line: " + line);
            return;
        }
        String dstPort = fields[6];
        String protocol = fields[7].equals("6") ? "tcp" : fields[7].equals("17") ? "udp" : "icmp";

        String key = (dstPort + "," + protocol).toLowerCase();
        String tag = lookup.getOrDefault(key, "untagged");

        tagCounts.merge(tag, 1, Integer::sum);
        portProtocolCounts.merge(key, 1, Integer::sum);
    }

    public Map<String, Integer> getTagCounts() {
        return tagCounts;
    }

    public Map<String, Integer> getPortProtocolCounts() {
        return portProtocolCounts;
    }
}