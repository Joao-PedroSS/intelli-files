package com.intelli_file.application.service;

import com.intelli_file.domain.rule.KeyWordRule.FolderConfig;
import com.intelli_file.infrastructure.config.ConfigRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageKeywordService {
    
    private final ConfigRepository repository = new ConfigRepository();

    public Map<String, List<String>> getExistingFolders() {
        try {
            Map<String, FolderConfig> rules = repository.loadKeywordRules();
            Map<String, List<String>> result = new HashMap<>();
            
            for (Map.Entry<String, FolderConfig> entry : rules.entrySet()) {
                result.put(entry.getKey(), entry.getValue().keywords);
            }
            return result;
        } catch (Exception e) {
            return new HashMap<>(); // Se falhar, devolve vazio em vez de crashar
        }
    }

    public void addFolder(String folderName) throws Exception {
        Map<String, FolderConfig> rules = repository.loadKeywordRules();
        
        if (rules.containsKey(folderName)) {
            throw new Exception("A pasta '" + folderName + "' já existe no sistema.");
        }
        
        // Cria uma nova configuração de pasta vazia
        FolderConfig newConfig = new FolderConfig();
        newConfig.keywords = new ArrayList<>();
        newConfig.extensions = new ArrayList<>();
        newConfig.extensions.add("*"); // Por padrão, aceita qualquer extensão até o utilizador restringir
        
        rules.put(folderName, newConfig);
        repository.saveKeywordRules(rules);
    }

    public void removeFolder(String folderName) throws Exception {
        Map<String, FolderConfig> rules = repository.loadKeywordRules();
        
        if (rules.remove(folderName) != null) {
            repository.saveKeywordRules(rules);
        } else {
            throw new Exception("A pasta '" + folderName + "' não existe.");
        }
    }

    // --- MÉTODOS ANTIGOS DE PALAVRAS-CHAVE MANTIDOS ---

    public void addKeyword(String folderName, String keyword) throws Exception {
        Map<String, FolderConfig> rules = repository.loadKeywordRules();
        if (rules.containsKey(folderName)) {
            FolderConfig config = rules.get(folderName);
            if (!config.keywords.contains(keyword.toLowerCase())) {
                config.keywords.add(keyword.toLowerCase());
                repository.saveKeywordRules(rules);
            }
        } else {
            throw new Exception("A pasta '" + folderName + "' não existe.");
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
            throw new Exception("A pasta '" + folderName + "' não existe.");
        }
    }
}