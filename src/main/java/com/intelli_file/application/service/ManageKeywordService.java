package com.intelli_file.application.service;

import com.intelli_file.domain.rule.KeyWordRule.FolderConfig;
import com.intelli_file.infrastructure.config.ConfigRepository;

import java.util.Map;

public class ManageKeywordService {
    
    private final ConfigRepository repository = new ConfigRepository();

    public void addKeyword(String folderName, String keyword) throws Exception {
        // 1. Carrega as regras atuais do HD
        Map<String, FolderConfig> rules = repository.loadKeywordRules();
        
        // 2. Modifica as regras em memória
        if (rules.containsKey(folderName)) {
            FolderConfig config = rules.get(folderName);
            if (!config.keywords.contains(keyword.toLowerCase())) {
                config.keywords.add(keyword.toLowerCase());
                // 3. Salva de volta no HD
                repository.saveKeywordRules(rules);
            }
        } else {
            throw new Exception("A pasta '" + folderName + "' não existe no config.json.");
        }
    }

    public void removeKeyword(String folderName, String keyword) throws Exception {
        Map<String, FolderConfig> rules = repository.loadKeywordRules();
        
        if (rules.containsKey(folderName)) {
            FolderConfig config = rules.get(folderName);
            if (config.keywords.remove(keyword.toLowerCase())) {
                repository.saveKeywordRules(rules);
            } else {
                throw new Exception("A palavra '" + keyword + "' não foi encontrada na pasta " + folderName);
            }
        } else {
            throw new Exception("A pasta '" + folderName + "' não existe no config.json.");
        }
    }
}