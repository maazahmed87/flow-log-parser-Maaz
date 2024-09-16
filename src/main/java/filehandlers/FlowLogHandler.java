package filehandlers;

import java.io.*;
import java.util.*;

public class FlowLogHandler extends FileHandler {
    private Map<String, String> lookup;
    private Map<String, Integer> tagCounts;
    private Map<String, Integer> portProtocolCounts;

    public FlowLogHandler(String filePath, Map<String, String> lookup) {
        super(filePath);
        this.lookup = lookup;
        this.tagCounts = new LinkedHashMap<>();
        this.portProtocolCounts = new LinkedHashMap<>();
    }

    @Override
    public void processFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] fields = line.trim().split("\\s+");

                if (fields.length < 14)
                    continue;

                String dstPort = fields[6];
                String protocol = fields[7].equals("6") ? "tcp" : fields[7].equals("17") ? "udp" : "icmp";

                String key = (dstPort + "," + protocol).toLowerCase();
                String tag = lookup.getOrDefault(key, "untagged");

                tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);

            }
        }
    }

    public Map<String, Integer> getTagCounts() {
        return tagCounts;
    }

    public Map<String, Integer> getPortProtocolCounts() {
        return portProtocolCounts;
    }
}