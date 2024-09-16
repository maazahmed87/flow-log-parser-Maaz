import filehandlers.*;
import outputgenerators.*;
import utils.ConfigurationManager;

import java.io.IOException;
import java.util.concurrent.*;

public class FlowLogParser {
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {

        ConfigurationManager configManager = new ConfigurationManager("config/config.properties");
        String flowLogPath = configManager.getProperty("flowlog.file.path", "data/flowlog.txt");
        String lookupPath = configManager.getProperty("lookup.file.path", "data/lookup.csv");
        String tagCountOutputPath = configManager.getProperty("tagcount.output.path", "output/tag_count_output.csv");
        String portProtocolOutputPath = configManager.getProperty("portprotocol.output.path",
                "output/port_protocol_output.csv");

        if (args.length > 0) {
            flowLogPath = args[0];
        }
        if (args.length > 1) {
            lookupPath = args[1];
        }

        System.out.println("INFO: Flow Log Path: " + flowLogPath);
        System.out.println("INFO: Lookup Path: " + lookupPath);
        System.out.println("INFO: Flow Log Parser started...");

        LookupTableHandler lookupHandler = new LookupTableHandler(lookupPath);
        if (!lookupHandler.isFileValid()) {
            System.err.println("ERROR: Invalid lookup table file: " + lookupPath);
            return;
        }

        FlowLogHandler flowLogHandler = new FlowLogHandler(flowLogPath, lookupHandler.getLookup());
        if (!flowLogHandler.isFileValid()) {
            System.err.println("ERROR: Invalid flow logs file: " + flowLogPath);
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        try {
            processLookupTable(lookupHandler);
            processFlowLogs(flowLogHandler, executor);
            generateOutputs(tagCountOutputPath, portProtocolOutputPath, flowLogHandler);
        } catch (IOException e) {
            System.err.println("ERROR: Error processing files: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdownExecutor(executor);
        }
    }

    private static void processLookupTable(LookupTableHandler lookupHandler) throws IOException {
        lookupHandler.processFile();
        System.out.println("INFO: Lookup table read successfully. Entries: " + lookupHandler.getLookup().size());
    }

    private static void processFlowLogs(FlowLogHandler flowLogHandler, ExecutorService executor) throws IOException {
        flowLogHandler.processFile(executor);
        System.out.println("INFO: Flow logs processed. Tag counts: " + flowLogHandler.getTagCounts().size() +
                ", Port-Protocol counts: " + flowLogHandler.getPortProtocolCounts().size());
    }

    private static void generateOutputs(String tagCountOutputPath, String portProtocolOutputPath,
            FlowLogHandler flowLogHandler) throws IOException {
        OutputGenerator tagCountOutput = new TagCountOutputGenerator(tagCountOutputPath, flowLogHandler.getTagCounts());
        tagCountOutput.generateOutput();
        System.out.println("INFO: Tag count output generated: " + tagCountOutputPath);

        OutputGenerator portProtocolOutput = new PortProtocolOutputGenerator(portProtocolOutputPath,
                flowLogHandler.getPortProtocolCounts());
        portProtocolOutput.generateOutput();
        System.out.println("INFO: Port-Protocol output generated: " + portProtocolOutputPath);
    }

    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.err.println("WARNING: Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            System.err.println("ERROR: Thread interruption during shutdown: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
