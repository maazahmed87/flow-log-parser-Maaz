package com.example.filehandlers;

import java.io.File;
import java.io.IOException;

public abstract class FileHandler {
    protected String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    // Check if the file is valid (exists, readable, and not empty)
    public boolean isFileValid() {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.canRead() && file.length() > 0;
    }

    // Abstract method to process the file
    public abstract void processFile() throws IOException;
}