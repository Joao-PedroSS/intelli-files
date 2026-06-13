package com.intelli_file.domain.rule;

import java.util.HashMap;
import java.util.Map;

public class ExtensionsRule {

    private static final Map<String, String> EXTENSION_MAP = new HashMap<>();

    static {
        EXTENSION_MAP.put("jpg", "images");
        EXTENSION_MAP.put("png", "images");
        EXTENSION_MAP.put("gif", "images");

        EXTENSION_MAP.put("mp4", "videos");
        EXTENSION_MAP.put("mkv", "videos");

        EXTENSION_MAP.put("pdf", "documents");
        EXTENSION_MAP.put("docx", "documents");
        EXTENSION_MAP.put("txt", "documents");
    }

    public static String resolveFolder(String extension) {
        return EXTENSION_MAP.getOrDefault(extension.toLowerCase(), "others");
    }
}