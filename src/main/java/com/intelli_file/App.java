package com.intelli_file;

import com.intelli_file.presentation.cli.CLIRunner;

public class App {
    public static void main(String[] args) {
        System.out.println("Iniciando o Intelli-File...\n");
        
        CLIRunner cli = new CLIRunner();
        cli.iniciarMenu();
    }
}