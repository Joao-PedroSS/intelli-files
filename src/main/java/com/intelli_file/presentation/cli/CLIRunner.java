package com.intelli_file.presentation.cli;

import com.intelli_file.application.controller.FileController;

public class CLIRunner {
    private final String[] args;
    private final FileController fileController;

    public CLIRunner(String[] arguments) {
        this.args = arguments;
        this.fileController = new FileController();
    }

    public void handleCommand() {
        if (this.args.length < 2) {
            System.out.println("Uso: <command> [argumentos...]");
            return;
        }

        switch (this.args[0]) {
            case "organize-ext" -> organizeByExtension();  
            case "organize-keyword" -> organizeByKeywords();
            // Nossos novos comandos:
            case "organize-smart" -> organizeSmart();
            case "add-keyword" -> addKeyword();
            case "remove-keyword" -> removeKeyword();
            default -> System.err.println("Comando desconhecido.");
        }
    }

    private void organizeByExtension() {
        if (this.args.length != 3) {
            System.out.println("Uso: <organize-ext> [source] [target]");
        }

        try {
            fileController.organizeByExtension(this.args[1], this.args[2]);
        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao organizar por extensão: " + e.getMessage());
        }
    }

    private void organizeByKeywords() {
        if (this.args.length != 3) {
            System.out.println("Uso: <organize-ext> [source] [target]");
        }

        try {
            fileController.organizeByKeywords(this.args[1], this.args[2]);
        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao organizar por palavra-chave: " + e.getMessage());
        }
    }

    private void organizeSmart() {
        if (this.args.length != 3) {
            System.out.println("Uso: organize-smart [source] [target]");
            return;
        }
        fileController.organizeSmart(this.args[1], this.args[2]);
    }

    private void addKeyword() {
        if (this.args.length != 3) {
            System.out.println("Uso: add-keyword [pasta] [palavra-chave]");
            return;
        }
        fileController.addKeywordConfig(this.args[1], this.args[2]);
    }

    private void removeKeyword() {
        if (this.args.length != 3) {
            System.out.println("Uso: remove-keyword [pasta] [palavra-chave]");
            return;
        }
        fileController.removeKeywordConfig(this.args[1], this.args[2]);
    }
}
