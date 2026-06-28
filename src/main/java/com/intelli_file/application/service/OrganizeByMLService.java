package com.intelli_file.application.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.intelli_file.domain.model.PredictionResult;
import com.intelli_file.infrastructure.aisystem.FastTextProcessManager;
import com.intelli_file.infrastructure.filesystem.FileMover;
import com.intelli_file.infrastructure.filesystem.FileScanner;

public class OrganizeByMLService {
    private final FileScanner scanner = new FileScanner();
    private final FileMover mover = new FileMover();

    public void execute(Path sourcePath, Path targetPath) throws IOException {
        List<Path> files = scanner.scan(sourcePath);
        FastTextProcessManager ffManager = new FastTextProcessManager(); 

        for (Path path : files) {
            try {
                PredictionResult result = ffManager.classifyFile(path);

                // AUMENTAR ISSO
                if (result.getProbability() > 0.0) {
                    Path finalDir = targetPath.resolve(result.getLabel());
                    System.out.println(result.toString());
                    mover.move(path, finalDir);
                }
            } catch (IOException | InterruptedException e) {
                throw new IOException(e.getMessage());
            }
        }
    }
}
