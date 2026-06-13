package com.intelli_file.infrastructure.filesystem;

import java.io.IOException;
import java.nio.file.*;

public class FileMover {
    public void move(Path source, Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        Path target = targetDir.resolve(source.getFileName());
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
}