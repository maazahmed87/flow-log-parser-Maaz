package com.example.outputgenerators;

import java.io.IOException;

public abstract class OutputGenerator {
    protected String filePath;

    public OutputGenerator(String filePath) {
        this.filePath = filePath;
    }

    // Abstract method to generate output file
    public abstract void generateOutput() throws IOException;
}