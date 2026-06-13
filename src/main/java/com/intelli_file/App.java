package com.intelli_file;

import com.intelli_file.presentation.cli.CLIRunner;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso incorreto, use o comando '-help' para obter a lista de comandos");
        }

        CLIRunner cliHandler = new CLIRunner(args);
        cliHandler.handleCommand();
    }
}