import java.io.*;
import java.util.*;

public class FlowLogParser {
    public static void main(String[] args) {
        String lookupTableFile = "lookup.csv";
        String flowLogsFile = "flowlog.txt";
        String outputFile = "output.txt";

        System.out.println("Flow Log Parser started...");

        try {
            Map<String, String> lookup = readLookupTable(lookupTableFile);
            System.out.println("Lookup table read successfully");
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
        System.err.println("Key,Value");
        lookup.forEach((k,v) -> System.out.println(k + "," + v));
        return lookup;
    }
}