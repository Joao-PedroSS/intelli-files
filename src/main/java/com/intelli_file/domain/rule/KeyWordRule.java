package com.intelli_file.domain.rule;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class KeyWordRule {

    public static class FolderConfig {
        public List<String> keywords;
        public List<String> extensions; 
    }
    
    private final Map<String, FolderConfig> rules;

    public KeyWordRule(Map<String, FolderConfig> rules) {
        this.rules = rules;
    }

    public String resolveFolder(String fileName, String fileExtension) {
        String lowerName = fileName.toLowerCase();
        String lowerExt = fileExtension.toLowerCase();

        // Substituímos traços e sublinhados por espaços. 
        String nomeLimpoParaBusca = lowerName.replaceAll("[_\\-]", " ");

        for (Map.Entry<String, FolderConfig> entry : rules.entrySet()) {
            String folderName = entry.getKey();
            FolderConfig config = entry.getValue();

            // A MÁGICA ESTÁ AQUI:
            // Protege contra listas vazias ou nulas do seu config.json
            boolean isExtensionValid = config.extensions == null || 
                                       config.extensions.isEmpty() || 
                                       config.extensions.contains("*") || 
                                       config.extensions.contains(lowerExt);

            if (!isExtensionValid) continue;

            // Blindagem extra: garante que a lista de palavras-chave não é nula
            if (config.keywords != null) {
                for (String keyword : config.keywords) {
                    String palavraBuscada = keyword.toLowerCase();
                    
                    // EXPRESSÃO REGULAR (REGEX):
                    String regex = "(?U).*\\b" + Pattern.quote(palavraBuscada) + "\\b.*";
                    
                    if (nomeLimpoParaBusca.matches(regex)) {
                        return folderName;
                    }
                }
            }
        }

        return "others";
    }
}