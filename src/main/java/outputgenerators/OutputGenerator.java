package outputgenerators;

import java.io.IOException;

public abstract class OutputGenerator {
    protected String filePath;

    public OutputGenerator(String filePath) {
        this.filePath = filePath;
    }

    public abstract void generateOutput() throws IOException;
}