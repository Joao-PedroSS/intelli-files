package com.intelli_file.application.controller;

import com.intelli_file.application.service.OrganizeByExtensionService;
import com.intelli_file.application.service.OrganizeByKeywordService;

import java.io.IOException;
import java.nio.file.Path;

public class FileController {
    public void organizeByExtension(String source, String target) throws RuntimeException {
        Path sourcePath = Path.of(source);
        Path targetPath = Path.of(target);

        try {
            new OrganizeByExtensionService().execute(sourcePath, targetPath);   
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void organizeByKeywords(String source, String target) throws RuntimeException {
        Path sourcePath = Path.of(source);
        Path targetPath = Path.of(target);

        try {
            new OrganizeByKeywordService().execute(sourcePath, targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
