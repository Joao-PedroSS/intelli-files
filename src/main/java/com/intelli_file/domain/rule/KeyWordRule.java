package com.intelli_file.domain.rule;

import java.util.List;
import java.util.Map;

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

        for (Map.Entry<String, FolderConfig> entry : rules.entrySet()) {
            String folderName = entry.getKey();
            FolderConfig config = entry.getValue();

            boolean isExtensionValid = config.extensions.contains("*") || config.extensions.contains(lowerExt);

            if (!isExtensionValid) continue;

            for (String keyword : config.keywords) {
                if (lowerName.contains(keyword.toLowerCase())) {
                    return folderName;
                }
            }
        }

        return "others";
    }
}