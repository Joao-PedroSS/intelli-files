package com.intelli_file.application.service;

import com.intelli_file.domain.model.FileItem;
import com.intelli_file.domain.rule.ExtensionsRule;
import com.intelli_file.domain.rule.KeyWordRule;
import com.intelli_file.infrastructure.config.ConfigRepository;
import com.intelli_file.infrastructure.filesystem.FileMover;
import com.intelli_file.infrastructure.filesystem.FileScanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class SmartOrganizeService {

    private final FileScanner scanner = new FileScanner();
    private final FileMover mover = new FileMover();

    public void execute(Path sourcePath, Path targetPath) throws IOException {
        List<Path> files = scanner.scan(sourcePath);
        
        // 1. O Repositório pega os dados do HD
        ConfigRepository repository = new ConfigRepository();
        Map<String, KeyWordRule.FolderConfig> regras = repository.loadKeywordRules();
        
        // 2. Injetamos os dados puros na Regra de Domínio
        KeyWordRule keywordRule = new KeyWordRule(regras);

        for (Path path : files) {
            FileItem file = new FileItem(path);
            String fileName = file.getPath().getFileName().toString();
            String extension = file.getExtension();

            // 3. A regra decide para onde vai (agora chamando pelo objeto instanciado, não pela classe)
            String folder = keywordRule.resolveFolder(fileName, extension);

            if (folder.equals("others")) {
                folder = ExtensionsRule.resolveFolder(extension);
            }

            Path finalDir = targetPath.resolve(folder);
            mover.move(path, finalDir);
        }
    }
}