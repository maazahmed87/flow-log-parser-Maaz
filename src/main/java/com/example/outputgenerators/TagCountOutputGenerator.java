package com.example.outputgenerators;

import java.io.*;
import java.util.Map;

public class TagCountOutputGenerator extends OutputGenerator {
    private Map<String, Integer> tagCounts;

    public TagCountOutputGenerator(String filePath, Map<String, Integer> tagCounts) {
        super(filePath);
        this.tagCounts = tagCounts;
    }

    // Generate the tag count output file
    @Override
    public void generateOutput() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Tag,Count\n");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }
}