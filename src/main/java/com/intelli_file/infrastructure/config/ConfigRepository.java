package com.intelli_file.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelli_file.domain.rule.KeyWordRule.FolderConfig;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    // Arquivo externo em ~/.intelli-files/config.json (funciona tanto em dev quanto em JAR)
    private static final Path EXTERNAL_CONFIG = Paths.get(
            System.getProperty("user.home"), ".intelli-files", "config.json");
            
    public Map<String, FolderConfig> loadKeywordRules() {
        try {
            if (Files.exists(EXTERNAL_CONFIG)) {
                return mapper.readValue(EXTERNAL_CONFIG.toFile(),
                        new TypeReference<Map<String, FolderConfig>>() {});
            }

            InputStream inputStream = getClass().getResourceAsStream("/config/config.json");
            if (inputStream != null) {
                return mapper.readValue(inputStream,
                        new TypeReference<Map<String, FolderConfig>>() {});
            } else {
                System.err.println("Aviso: config.json não encontrado. Retornando mapa vazio.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o JSON: " + e.getMessage());
        }
        return new HashMap<>();
    }

    /**
     * Salva as regras no arquivo externo (~/.intelli-files/config.json).
     * Funciona em qualquer ambiente, inclusive JAR empacotado.
     */
    public void saveKeywordRules(Map<String, FolderConfig> rules) throws Exception {
        Files.createDirectories(EXTERNAL_CONFIG.getParent());
        mapper.writerWithDefaultPrettyPrinter().writeValue(EXTERNAL_CONFIG.toFile(), rules);
    }
}
