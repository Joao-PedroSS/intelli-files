package com.intelli_file.application.service;

import com.intelli_file.domain.model.FileItem;
import com.intelli_file.domain.rule.KeyWordRule;
import com.intelli_file.infrastructure.config.ConfigRepository;
import com.intelli_file.infrastructure.filesystem.FileMover;
import com.intelli_file.infrastructure.filesystem.FileScanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class OrganizeByKeywordService {
    
    private final FileScanner scanner;
    private final FileMover mover;
    private final ConfigRepository repository;

    public OrganizeByKeywordService() {
        this(new FileScanner(), new FileMover(), new ConfigRepository());
    }

    public OrganizeByKeywordService(FileScanner scanner, FileMover mover, ConfigRepository repository) {
        this.scanner = scanner;
        this.mover = mover;
        this.repository = repository;
    }

    public void execute(Path sourcePath, Path targetPath) throws IOException {
        List<Path> files = scanner.scan(sourcePath);

        // 1. O Repositório carrega as regras físicas (do arquivo JSON) para a memória do Java
        Map<String, KeyWordRule.FolderConfig> regras = repository.loadKeywordRules();

        // 2. A nossa Regra de Domínio purificada é instanciada recebendo esses dados prontos
        KeyWordRule keywordRule = new KeyWordRule(regras);

        for (Path path : files) {
            FileItem file = new FileItem(path);
            String fileName = file.getPath().getFileName().toString();
            String extension = file.getExtension();

            // 3. A regra decide o nome da pasta destino, sem encostar em nenhuma biblioteca de JSON
            String folder = keywordRule.resolveFolder(fileName, extension);

            Path finalDir = targetPath.resolve(folder);
            mover.move(path, finalDir);
        }
    }
}