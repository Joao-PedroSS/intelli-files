package com.intelli_file.domain.rule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWordRule {

    public static class FolderConfig {
        public List<String> keywords;
        public List<String> extensions;
    }

    private static Map<String, FolderConfig> KEYWORD_RULES = new HashMap<>();

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = KeyWordRule.class.getResourceAsStream("/config/config.json");

            if (inputStream != null) {
                KEYWORD_RULES = mapper.readValue(inputStream, new TypeReference<Map<String, FolderConfig>>() {});
            } else {
                throw new RuntimeException("Aviso: Arquivo /config/config.json não encontrado. Usando regras vazias de fallback.");
            }
        } catch (Exception e) {
            System.err.println("Erro durante a leitura do arquivo de configuração (config.json): " + e.getMessage());
        }
    }

    public static String resolveFolder(String fileName, String fileExtension) {
        String lowerName = fileName.toLowerCase();
        String lowerExt = fileExtension.toLowerCase();

        for (Map.Entry<String, FolderConfig> entry : KEYWORD_RULES.entrySet()) {
            String folderName = entry.getKey();
            FolderConfig config = entry.getValue();

            boolean isExtensionValid = config.extensions.contains("*") || config.extensions.contains(lowerExt);

            if (!isExtensionValid) {
                continue; 
            }

            for (String keyword : config.keywords) {
                if (lowerName.contains(keyword.toLowerCase())) {
                    return folderName; 
                }
            }
        }

        return "others"; 
    }
}