package com.intelli_file.domain.model;

import java.nio.file.Path;

public class FileItem {

    private final Path path;

    public FileItem(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public String getExtension() {
        String name = path.getFileName().toString();
        int i = name.lastIndexOf('.');
        return (i > 0) ? name.substring(i + 1) : "";
    }
}