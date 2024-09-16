package outputgenerators;

import java.io.*;
import java.util.Map;

public class PortProtocolOutputGenerator extends OutputGenerator {
    private Map<String, Integer> portProtocolCounts;

    public PortProtocolOutputGenerator(String filePath, Map<String, Integer> portProtocolCounts) {
        super(filePath);
        this.portProtocolCounts = portProtocolCounts;
    }

    @Override
    public void generateOutput() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Port/Protocol,Count\n");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }
}