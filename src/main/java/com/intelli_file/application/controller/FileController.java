package com.intelli_file.application.controller;

import com.intelli_file.application.service.ManageKeywordService;
import com.intelli_file.application.service.OrganizeByExtensionService;
import com.intelli_file.application.service.OrganizeByKeywordService;
import com.intelli_file.application.service.SmartOrganizeService;

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

    public void organizeSmart(String source, String target) throws RuntimeException {
        Path sourcePath = Path.of(source);
        Path targetPath = Path.of(target);

        try {
            new SmartOrganizeService().execute(sourcePath, targetPath);
        } catch (IOException e) {
            throw new RuntimeException("Erro na organização inteligente: " + e.getMessage());
        }
    }

    // --- MÉTODOS ATUALIZADOS ABAIXO ---

    public void addKeywordConfig(String folder, String keyword) {
        try {
            // Agora usamos o serviço dedicado a isso!
            new ManageKeywordService().addKeyword(folder, keyword);
            System.out.println("Sucesso: Palavra-chave '" + keyword + "' adicionada à pasta '" + folder + "'.");
        } catch (Exception e) {
            System.err.println("Erro ao adicionar palavra-chave: " + e.getMessage());
        }
    }

    public void removeKeywordConfig(String folder, String keyword) {
        try {
            // Agora usamos o serviço dedicado a isso!
            new ManageKeywordService().removeKeyword(folder, keyword);
            System.out.println("Sucesso: Palavra-chave '" + keyword + "' removida da pasta '" + folder + "'.");
        } catch (Exception e) {
            System.err.println("Erro ao remover palavra-chave: " + e.getMessage());
        }
    }
}