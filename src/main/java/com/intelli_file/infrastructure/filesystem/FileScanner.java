package com.intelli_file.infrastructure.filesystem;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileScanner {

    public List<Path> scan(Path directory) throws IOException {
        try (Stream<Path> stream = Files.walk(directory)) {
            return stream
                    .filter(path -> !Files.isDirectory(path))
                    .collect(Collectors.toList());
        }
    }
}