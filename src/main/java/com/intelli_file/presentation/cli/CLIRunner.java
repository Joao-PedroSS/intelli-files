package com.intelli_file.presentation.cli;

import javax.management.RuntimeErrorException;

public class CLIRunner {
    private final String[] args;

    public CLIRunner(String[] arguments) {
        this.args = arguments;
    }

    public void handleCommand() {
        if (this.args.length < 2) {
            System.out.println("Uso: --[command] <argument>");
            return;
        }

        switch (this.args[0]) {
            case "--organize-ext" -> organizeByExtension();  
            default -> throw new AssertionError();
        }
    }

    private void organizeByExtension() {
        if (this.args.length != 3) {
            System.out.println("Uso: --organize-ext <source> <target>");
        }

        try {
            throw new RuntimeErrorException(new Error("Função não implementada"));
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
