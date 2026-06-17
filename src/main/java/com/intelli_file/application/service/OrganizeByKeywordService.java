package com.intelli_file.application.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.intelli_file.domain.model.FileItem;
import com.intelli_file.domain.rule.KeyWordRule;
import com.intelli_file.infrastructure.filesystem.FileMover;
import com.intelli_file.infrastructure.filesystem.FileScanner;

public class OrganizeByKeywordService {
    private final FileScanner scanner = new FileScanner();
    private final FileMover mover = new FileMover();

    public void execute(Path sourcePath, Path targetPath) throws IOException {
        List<Path> files = scanner.scan(sourcePath);

        for (Path path : files) {
            FileItem file = new FileItem(path);

            String folder = KeyWordRule.resolveFolder(file.getPath().getFileName().toString(), file.getExtension());

            Path finalDir = targetPath.resolve(folder);

            mover.move(path, finalDir);
        }
    }
}
