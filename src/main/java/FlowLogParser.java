import filehandlers.*;
import outputgenerators.*;
import java.io.IOException;

public class FlowLogParser {
    public static void main(String[] args) {
        String lookupTableFile = "data/lookup.csv";
        String flowLogsFile = "data/flowlog.txt";
        String tagCountOutputFile = "output/tag_count_output.csv";
        String portProtocolOutputFile = "output/port_protocol_output.csv";

        System.out.println("Flow Log Parser started...");

        LookupTableHandler lookupHandler = new LookupTableHandler(lookupTableFile);
        if (!lookupHandler.isFileValid()) {
            System.out.println("Invalid lookup table file: " + lookupTableFile);
            return;
        }
        System.out.println("Lookup table file is valid.");

        FlowLogHandler flowLogHandler = new FlowLogHandler(flowLogsFile, lookupHandler.getLookup());
        if (!flowLogHandler.isFileValid()) {
            System.out.println("Invalid flow logs file: " + flowLogsFile);
            return;
        }
        System.out.println("Flow logs file is valid.");

        try {
            lookupHandler.processFile();
            System.out.println("Lookup table read successfully. Entries: " + lookupHandler.getLookup().size());

            flowLogHandler.processFile();
            System.out.println("Flow logs processed. Tag counts: " + flowLogHandler.getTagCounts().size() +
                    ", Port-Protocol counts: " + flowLogHandler.getPortProtocolCounts().size());

            OutputGenerator tagCountOutput = new TagCountOutputGenerator(tagCountOutputFile,
                    flowLogHandler.getTagCounts());
            tagCountOutput.generateOutput();
            System.out.println("Tag count output generated: " + tagCountOutputFile);

            OutputGenerator portProtocolOutput = new PortProtocolOutputGenerator(portProtocolOutputFile,
                    flowLogHandler.getPortProtocolCounts());
            portProtocolOutput.generateOutput();
            System.out.println("Port-Protocol output generated: " + portProtocolOutputFile);

            System.out.println("Output files generated successfully");

        } catch (IOException e) {
            System.out.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}