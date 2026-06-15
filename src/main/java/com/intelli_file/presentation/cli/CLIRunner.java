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
            System.out.println("Uso: <command> [argument]");
            return;
        }

        switch (this.args[0]) {
            case "organize-ext" -> organizeByExtension();  
            default -> throw new AssertionError();
        }
    }

    private void organizeByExtension() {
        if (this.args.length != 3) {
            throw new RuntimeException("Uso: <organize-ext> [source] [target]");
        }

        try {
            fileController.organizeByExtension(this.args[1], this.args[2]);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
