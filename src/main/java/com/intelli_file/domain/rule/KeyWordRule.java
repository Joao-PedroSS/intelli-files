package com.intelli_file.domain.rule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWordRule {

    // Classe auxiliar para mapear a estrutura exata do JSON de configuração.
    // Utilizada para refletir as propriedades "keywords" e "extensoes" de cada nó.
    public static class FolderConfig {
        public List<String> keywords;
        public List<String> extensoes;
    }

    // Armazena as regras de roteamento em memória.
    // Chave: Nome da pasta de destino (String) | Valor: Configurações da pasta (FolderConfig)
    private static Map<String, FolderConfig> KEYWORD_RULES = new HashMap<>();

    // Bloco estático para inicialização das regras na carga da classe.
    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = KeyWordRule.class.getResourceAsStream("/config/config.json");

            if (inputStream != null) {
                // Deserializa o JSON dinamicamente para o Map com base na referência de tipo especificada.
                KEYWORD_RULES = mapper.readValue(inputStream, new TypeReference<Map<String, FolderConfig>>() {});
            } else {
                System.err.println("Aviso: Arquivo /config/config.json não encontrado. Usando regras vazias de fallback.");
            }
        } catch (Exception e) {
            System.err.println("Erro durante a leitura do arquivo de configuração (config.json): " + e.getMessage());
        }
    }

    /**
     * Avalia o nome e a extensão do arquivo contra as regras de negócio carregadas.
     * Retorna o diretório alvo correspondente se os critérios de extensão e palavra-chave forem satisfeitos.
     * * @param fileName Nome base do arquivo
     * @param fileExtension Extensão do arquivo
     * @return String contendo o nome da pasta destino ou "others" como fallback.
     */
    public static String resolveFolder(String fileName, String fileExtension) {
        // Padronização para case-insensitive
        String lowerName = fileName.toLowerCase();
        String lowerExt = fileExtension.toLowerCase();

        // Itera sobre as configurações de cada diretório mapeado no JSON
        for (Map.Entry<String, FolderConfig> entry : KEYWORD_RULES.entrySet()) {
            String folderName = entry.getKey();
            FolderConfig config = entry.getValue();

            // 1. Validação de extensão: aprova se a lista contiver a extensão exata ou o curinga "*"
            boolean isExtensionValid = config.extensoes.contains("*") || config.extensoes.contains(lowerExt);

            if (!isExtensionValid) {
                // Interrompe a validação atual e avança para o próximo diretório se a extensão for inválida
                continue; 
            }

            // 2. Validação de palavra-chave (substring matching)
            for (String keyword : config.keywords) {
                if (lowerName.contains(keyword.toLowerCase())) {
                    // Condições de negócio atendidas (Extensão + Keyword). Retorna o diretório alvo.
                    return folderName; 
                }
            }
        }

        // Fallback padrão de roteamento caso nenhuma regra seja satisfeita
        return "others"; 
    }
}