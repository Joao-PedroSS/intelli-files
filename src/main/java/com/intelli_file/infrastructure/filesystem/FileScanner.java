package com.intelli_file.infrastructure.filesystem;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    public List<Path> scan(Path directory) throws IOException {
        List<Path> files = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    files.add(path);
                }
            }
        }

        return files;
    }
}