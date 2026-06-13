package com.intelli_file.presentation.cli;

public class CLIRunner {
    private final String[] args;

    public CLIRunner(String[] arguments) {
        this.args = arguments;
    }   

    public void handleCommand() {
        System.out.println("cebolao");
        System.out.println(this.args[0]);
    }
}
