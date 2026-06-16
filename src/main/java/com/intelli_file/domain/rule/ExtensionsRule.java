package com.intelli_file.domain.rule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExtensionsRule {

    private static Map<String, String> EXTENSION_MAP = new HashMap<>();

    // Bloco estático para carregar as configurações do JSON apenas uma vez na inicialização
    static {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Busca o arquivo JSON na raiz do classpath (pasta resources)
            InputStream inputStream = ExtensionsRule.class.getResourceAsStream("/config/Extensions.json");

            if (inputStream != null) {
                // Lê o arquivo e converte automaticamente a estrutura do JSON para o Map
                EXTENSION_MAP = mapper.readValue(inputStream, new TypeReference<Map<String, String>>() {});
            } else {
                System.err.println("Aviso: Arquivo /config/Extensions.json não encontrado. Usando regras vazias.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao tentar ler o arquivo de configuração JSON: " + e.getMessage());
        }
    }

    public static String resolveFolder(String extension) {
        // Retorna a pasta mapeada do JSON ou "others" como fallback padrão
        return EXTENSION_MAP.getOrDefault(extension.toLowerCase(), "others");
    }
}