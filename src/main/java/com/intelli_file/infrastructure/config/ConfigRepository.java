package com.intelli_file.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelli_file.domain.rule.KeyWordRule.FolderConfig; // Importamos a estrutura do JSON da nossa regra

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigRepository {

    private final ObjectMapper mapper = new ObjectMapper();
    // Caminho físico usado para salvar as alterações durante o desenvolvimento
    private final String FILE_PATH = "src/main/resources/config/config.json"; 

    /**
     * Vai no HD, lê o JSON e devolve os dados prontos para o Java usar.
     */
    public Map<String, FolderConfig> loadKeywordRules() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/config/config.json");
            if (inputStream != null) {
                return mapper.readValue(inputStream, new TypeReference<Map<String, FolderConfig>>() {});
            } else {
                System.err.println("Aviso: config.json não encontrado. Retornando mapa vazio.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o JSON: " + e.getMessage());
        }
        return new HashMap<>();
    }

    /**
     * Recebe os dados do Java e salva de volta no arquivo físico.
     */
    public void saveKeywordRules(Map<String, FolderConfig> rules) throws Exception {
        File configFile = new File(FILE_PATH);
        mapper.writerWithDefaultPrettyPrinter().writeValue(configFile, rules);
    }
}