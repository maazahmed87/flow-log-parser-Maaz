package com.example.filehandlers;

import java.io.*;
import java.util.*;

public class LookupTableHandler extends FileHandler {
    private Map<String, String> lookup;

    public LookupTableHandler(String filePath) {
        super(filePath);
        this.lookup = new HashMap<>();
    }

    // Process the lookup table
    @Override
    public void processFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.trim().split(",");
                if (parts.length == 3) {
                    String key = (parts[0] + "," + parts[1]).toLowerCase();
                    lookup.put(key, parts[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: Can't read file: " + filePath);
            throw e;
        }
        
    }

    public Map<String, String> getLookup() {
        return lookup;
    }
}