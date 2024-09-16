package com.example.filehandlers;

import java.io.File;
import java.io.IOException;

public abstract class FileHandler {
    protected String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    public boolean isFileValid() {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.canRead() && file.length() > 0;
    }

    public abstract void processFile() throws IOException;
}