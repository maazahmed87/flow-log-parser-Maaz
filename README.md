# Flow Log Parser

This program parses flow logs and maps them to predefined tags using a lookup table. It processes the log data and generates two output files:
1. **Tag Counts**: Summarizes how many times each tag appears.
2. **Port-Protocol Counts**: Counts how often each port-protocol combination occurs.

## Key Features
1. **Efficient Parsing**: Processes large flow logs efficiently.
2. **Tag Mapping**: Maps port-protocol combinations to predefined tags using a lookup table.
3. **Multithreading**: Uses multithreading to improve performance for larger datasets.
4. **File Output**: Produces detailed summaries for both tag counts and port-protocol combinations.

### File Structure
- `flowlog.txt`: Contains flow log entries.
- `lookup.csv`: Maps port-protocol combinations to tags.
- `config.properties`: Configuration file with file paths for inputs and outputs.

### Usage
#### Prerequisites
- Java 8 or higher
- Ensure correct paths are set in config/config.properties or pass them as command-line arguments.

How to run the program:
1. Compile and test:
```bash
mvn compile
mvn test
```
2. Package
```bash
mvn package

```

3. Run the program with default file paths:
```bash

java -cp target/flow-log-parser-maaz-1.0-SNAPSHOT.jar com.example.FlowLogParser

```

3. Run with command-line arguments:
```bash

java -cp target/flow-log-parser-maaz-1.0-SNAPSHOT.jar com.example.FlowLogParser /path/to/flowlog.txt /path/to/lookup.csv
```
