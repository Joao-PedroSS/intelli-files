package com.intelli_file.application.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.intelli_file.domain.model.FileItem;
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
            FileItem file = new FileItem(path);
            if (file.getExtension().equals("txt")) { 
                try {
                    PredictionResult result = ffManager.classifyFile(path);

                    Path finalDir = targetPath.resolve(result.getLabel());
                    System.out.println(result.toString());
                    mover.move(path, finalDir);
                    continue;
                } catch (IOException | InterruptedException e) {
                    throw new IOException(e.getMessage());
                }
            }

            Path finalDir = targetPath.resolve("Outros");
            System.out.println("Outros (1.0)");
            mover.move(path, finalDir);
        }
    }
}
