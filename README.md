javac -d target/classes src/main/java/filehandlers/*.java src/main/java/outputgenerators/*.java src/main/java/utils/ConfigurationManager.java src/main/java/FlowLogParser.java
java -cp target/classes FlowLogParser
