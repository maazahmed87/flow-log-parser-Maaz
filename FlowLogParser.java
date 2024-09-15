import java.io.*;
import java.util.*;

public class FlowLogParser {
    public static void main(String[] args) {
        String lookupTableFile = "lookup.csv";
        String flowLogsFile = "flowlog.txt";
        String outputFile = "output.csv";

        System.out.println("Flow Log Parser started...");

        try {
            Map<String, String> lookup = readLookupTable(lookupTableFile);
            System.out.println("Lookup table read successfully");
            
            Map<String, Integer> tagCounts = new LinkedHashMap<>();
            Map<String, Integer> portProtocolCounts = new LinkedHashMap<>();

            parseFlowLogs(flowLogsFile, lookup, tagCounts, portProtocolCounts);
            writeOutput(outputFile, tagCounts, portProtocolCounts);

        } catch (IOException e) {
            System.out.println("Error reading lookup table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<String, String> readLookupTable(String filePath) throws IOException {
        Map<String, String> lookup = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = parts[0] + "," + parts[1];
                    lookup.put(key, parts[2]);
                }
            }
        }
        System.out.println("Lookup table: ");
        System.out.println("Key,Value");
        lookup.forEach((k,v) -> System.out.println(k + "," + v));
        return lookup;
    }

    private static void parseFlowLogs(String filePath, Map<String, String> lookup, Map<String, Integer> tagCounts,
            Map<String, Integer> portProtocolCounts) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.trim().split("\\s+");

                if (fields.length < 14)
                    continue;

                String dstPort = fields[6];
                String protocol = fields[7].equals("6") ? "tcp" : fields[7].equals("17") ? "udp" : "icmp";

                String key = dstPort + "," + protocol;
                String tag = lookup.getOrDefault(key, "untagged");

                tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);

            }
        }

        tagCounts.forEach((k, v) -> System.out.println(k + ": " + v));
        portProtocolCounts.forEach((k, v) -> System.err.println(k + ": " + v));
    }
    
    public static void writeOutput(String filePath, Map<String, Integer> tagCounts,
            Map<String, Integer> portProtocolCounts) throws IOException {

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Tag Counts:");
            writer.println("Tag, Count");

            tagCounts.forEach((k, v) -> writer.println(k + "," + v));
            
            writer.println("\nPort/Protocol Combination Counts: ");
            writer.println("Port, Protocol, Count");

            portProtocolCounts.forEach((k, v) -> {
                String[] parts = k.split(",");
                writer.println(parts[0] + "," + parts[1] + "," + v);
            });
        }
    }
}