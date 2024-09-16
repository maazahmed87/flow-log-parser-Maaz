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
- Apache Maven
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


### Assumptions and Discrepancies

#### Field Mapping in Flow Logs:
Based on the documentation and my understanding, the fields in the flow log are mapped as follows:
- **Destination Port (`dstport`):** 7th field
- **Protocol:** 8th field

If this mapping is incorrect according to the actual data format, it could result in discrepancies between the expected output and the sample output provided.

#### Sample Output Discrepancy:
- **`sv_P4` Tag Issue:** According to the lookup table, the `sv_P4` tag is associated with `dstport` 22 and `tcp` protocol. However, in the provided sample output, `sv_P4` appears in the tag counts despite the combination not being present in the provided flow logs. This suggests that either the provided sample output might be incorrect or there may be additional information not included in the sample logs.

#### Assumptions:
- The flow logs are processed with the assumption that the 7th field is the destination port and the 8th field is the protocol.
- The protocol field is interpreted as follows:
  - `6` maps to `tcp`
  - `17` maps to `udp`
  - Any other value (e.g., `1`) is considered `icmp`

