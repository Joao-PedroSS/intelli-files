package com.intelli_file.application.controller;

import com.intelli_file.application.service.OrganizeByExtensionService;
import java.io.IOException;
import java.nio.file.Path;

public class FileController {
    public void organizeByExtension(String source, String target) {
        Path sourcePath = Path.of(source);
        Path targetPath = Path.of(target);

        try {
            new OrganizeByExtensionService().execute(sourcePath, targetPath);   
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
