package com.intelli_file.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelli_file.domain.rule.KeyWordRule.FolderConfig;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    // ALTERADO: Agora salva na raiz do projeto. O arquivo vai aparecer direto no seu VS Code!
    private static final Path EXTERNAL_CONFIG = Paths.get("config.json");
            
    public Map<String, FolderConfig> loadKeywordRules() {
        try {
            // Se o config.json local na raiz existir, lê ele com UTF-8
            if (Files.exists(EXTERNAL_CONFIG)) {
                try (Reader reader = Files.newBufferedReader(EXTERNAL_CONFIG, StandardCharsets.UTF_8)) {
                    return mapper.readValue(reader, new TypeReference<Map<String, FolderConfig>>() {});
                }
            }

            // Se não existir na raiz, pega o modelo padrão de fábrica dos resources
            InputStream inputStream = getClass().getResourceAsStream("/config/config.json");
            if (inputStream != null) {
                return mapper.readValue(inputStream, new TypeReference<Map<String, FolderConfig>>() {});
            } else {
                System.err.println("Aviso: config.json de fábrica não encontrado. Retornando mapa vazio.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o JSON: " + e.getMessage());
        }
        return new HashMap<>();
    }

    /**
     * Salva as configurações direto na raiz do projeto em formato UTF-8 puro.
     */
    public void saveKeywordRules(Map<String, FolderConfig> rules) throws Exception {
        // O Writer com StandardCharsets.UTF_8 força o Windows a aceitar 'ç' e 'ã' sem abortar a gravação
        try (Writer writer = Files.newBufferedWriter(EXTERNAL_CONFIG, StandardCharsets.UTF_8)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, rules);
            System.out.println("-> Sucesso! Configurações salvas na raiz do projeto.");
        }
    }
}