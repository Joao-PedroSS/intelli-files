package com.intelli_file.presentation.cli;

import com.intelli_file.application.controller.FileController;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CLIRunner {
    private final FileController controller;
    private final Scanner scanner;

    public CLIRunner() {
        this.controller = new FileController();
        this.scanner = new Scanner(System.in);
    }

    public void iniciarMenu() {
        boolean rodando = true;

        while (rodando) {
            System.out.println("=======================================");
            System.out.println("        INTELLIFILES MANAGER           ");
            System.out.println("=======================================");
            System.out.println("--- ORGANIZAÇÃO DE FICHEIROS ---");
            System.out.println("1. Organização Inteligente (Cascata)");
            System.out.println("2. Organizar por Palavra-chave");
            System.out.println("3. Organizar por Extensão");
            System.out.println("\n--- GESTÃO DE REGRAS ---");
            System.out.println("4. Ver Pastas e Palavras-chave atuais");
            System.out.println("5. Adicionar nova Pasta");
            System.out.println("6. Remover Pasta existente");
            System.out.println("7. Adicionar nova Palavra-chave");
            System.out.println("8. Remover Palavra-chave");
            System.out.println("0. Sair");
            System.out.println("=======================================");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> organizarSmart();
                case "2" -> organizarKeywords();
                case "3" -> organizarExtensao();
                case "4" -> mostrarRegrasAtuais();
                case "5" -> adicionarPasta();
                case "6" -> removerPasta();
                case "7" -> adicionarKeyword();
                case "8" -> removerKeyword();
                case "0" -> {
                    System.out.println("Encerrando o IntelliFiles. Até logo!");
                    rodando = false;
                }
                default -> System.err.println("Opção inválida! Digite um número de 0 a 8.");
            }
        }
    }

    // --- MÉTODOS DE ORGANIZAÇÃO ATUALIZADOS ---
    private void organizarSmart() {
        System.out.println("Abra a janela que pipocou e selecione a pasta de ORIGEM...");
        String origem = FolderChooser.selecionarPasta("Intelli-File: Selecione a pasta de ORIGEM");
        if (origem == null) {
            System.out.println("Operação cancelada pelo usuário.");
            return; // Interrompe se o usuário fechar a janela
        }

        System.out.println("Abra a janela que pipocou e selecione a pasta de DESTINO...");
        String destino = FolderChooser.selecionarPasta("Intelli-File: Selecione a pasta de DESTINO");
        if (destino == null) {
            System.out.println("Operação cancelada pelo usuário.");
            return;
        }

        controller.organizeSmart(origem, destino);
        System.out.println("-> Organização concluída com sucesso!");
    }

    private void organizarKeywords() {
        System.out.println("Abra a janela que pipocou e selecione a pasta de ORIGEM...");
        String origem = FolderChooser.selecionarPasta("Intelli-File: Selecione a pasta de ORIGEM");
        if (origem == null) return;

        System.out.println("Abra a janela que pipocou e selecione a pasta de DESTINO...");
        String destino = FolderChooser.selecionarPasta("Intelli-File: Selecione a pasta de DESTINO");
        if (destino == null) return;

        controller.organizeByKeywords(origem, destino);
        System.out.println("-> Organização concluída com sucesso!");
    }

    private void organizarExtensao() {
        System.out.println("Abra a janela que pipocou e selecione a pasta de ORIGEM...");
        String origem = FolderChooser.selecionarPasta("Intelli-File: Selecione a pasta de ORIGEM");
        if (origem == null) return;

        System.out.println("Abra a janela que pipocou e selecione a pasta de DESTINO...");
        String destino = FolderChooser.selecionarPasta("Intelli-File: Selecione a pasta de DESTINO");
        if (destino == null) return;

        controller.organizeByExtension(origem, destino);
        System.out.println("-> Organização concluída com sucesso!");
    }

    // --- MÉTODOS DE GESTÃO VISUAL ---
    
    private void mostrarRegrasAtuais() {
        System.out.println("\n--- PASTAS E PALAVRAS-CHAVE ATUAIS ---");
        Map<String, List<String>> pastas = controller.getFoldersAndKeywords();
        
        if (pastas.isEmpty()) {
            System.out.println("(Nenhuma regra configurada no momento)");
            return;
        }

        for (Map.Entry<String, List<String>> entry : pastas.entrySet()) {
            System.out.println("Pasta: [" + entry.getKey() + "]");
            System.out.println("   Palavras-chave: " + entry.getValue());
        }
        System.out.println("--------------------------------------");
    }

    private void adicionarPasta() {
        mostrarRegrasAtuais();
        System.out.print("Digite o NOME da nova pasta: ");
        String pasta = scanner.nextLine();
        controller.addFolderConfig(pasta);
    }

    private void removerPasta() {
        mostrarRegrasAtuais();
        System.out.print("Digite o NOME da pasta que deseja apagar: ");
        String pasta = scanner.nextLine();
        controller.removeFolderConfig(pasta);
    }

    private void adicionarKeyword() {
        mostrarRegrasAtuais();
        System.out.print("Em qual PASTA deseja adicionar a palavra? (ex: Faculdade): ");
        String pasta = scanner.nextLine();
        System.out.print("Digite a nova PALAVRA-CHAVE: ");
        String palavra = scanner.nextLine();
        controller.addKeywordConfig(pasta, palavra);
    }

    private void removerKeyword() {
        mostrarRegrasAtuais();
        System.out.print("De qual PASTA deseja remover a palavra? (ex: Faculdade): ");
        String pasta = scanner.nextLine();
        System.out.print("Digite a PALAVRA-CHAVE a ser removida: ");
        String palavra = scanner.nextLine();
        controller.removeKeywordConfig(pasta, palavra);
    }
}