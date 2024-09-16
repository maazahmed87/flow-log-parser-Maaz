package filehandlers;

import java.io.*;
import java.util.*;

public class LookupTableHandler extends FileHandler {
    private Map<String, String> lookup;

    public LookupTableHandler(String filePath) {
        super(filePath);
        this.lookup = new HashMap<>();
    }

    @Override
    public void processFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = (parts[0] + "," + parts[1]).toLowerCase();
                    lookup.put(key, parts[2]);
                }
            }
        }
    }

    public Map<String, String> getLookup() {
        return lookup;
    }
}