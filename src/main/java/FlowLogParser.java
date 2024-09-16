import filehandlers.*;
import outputgenerators.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class FlowLogParser {
    public static void main(String[] args) {
        Properties props = new Properties();

        String flowLogPath = "data/flowlog.txt";
        String lookupPath = "data/lookup.csv";
        String tagCountOutputPath = "output/tag_count_output.csv";
        String portProtocolOutputPath = "output/port_protocol_output.csv";

        try (InputStream input = new FileInputStream("config/config.properties")) {
            props.load(input);
            flowLogPath = props.getProperty("flowlog.file.path", flowLogPath);
            lookupPath = props.getProperty("lookup.file.path", lookupPath);
            tagCountOutputPath = props.getProperty("tagcount.output.path", tagCountOutputPath);
            portProtocolOutputPath = props.getProperty("portprotocol.output.path", portProtocolOutputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (args.length > 0) {
            flowLogPath = args[0];
        }
        if (args.length > 1) {
            lookupPath = args[1];
        }
        
        System.out.println("Flow Log Path: " + flowLogPath);
        System.out.println("Lookup Path: " + lookupPath);
        

        System.out.println("Flow Log Parser started...");

        LookupTableHandler lookupHandler = new LookupTableHandler(lookupPath);
        if (!lookupHandler.isFileValid()) {
            System.out.println("Invalid lookup table file: " + lookupPath);
            return;
        }
        System.out.println("Lookup table file is valid.");

        FlowLogHandler flowLogHandler = new FlowLogHandler(flowLogPath, lookupHandler.getLookup());
        if (!flowLogHandler.isFileValid()) {
            System.out.println("Invalid flow logs file: " + flowLogPath);
            return;
        }
        System.out.println("Flow logs file is valid.");

        try {
            lookupHandler.processFile();
            System.out.println("Lookup table read successfully. Entries: " + lookupHandler.getLookup().size());

            flowLogHandler.processFile();
            System.out.println("Flow logs processed. Tag counts: " + flowLogHandler.getTagCounts().size() +
                    ", Port-Protocol counts: " + flowLogHandler.getPortProtocolCounts().size());

            OutputGenerator tagCountOutput = new TagCountOutputGenerator(tagCountOutputPath,
                    flowLogHandler.getTagCounts());
            tagCountOutput.generateOutput();
            System.out.println("Tag count output generated: " + tagCountOutputPath);

            OutputGenerator portProtocolOutput = new PortProtocolOutputGenerator(portProtocolOutputPath,
                    flowLogHandler.getPortProtocolCounts());
            portProtocolOutput.generateOutput();
            System.out.println("Port-Protocol output generated: " + portProtocolOutputPath);

            System.out.println("Output files generated successfully");

        } catch (IOException e) {
            System.out.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}