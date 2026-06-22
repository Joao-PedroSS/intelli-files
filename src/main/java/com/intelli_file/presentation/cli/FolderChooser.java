package com.intelli_file.presentation.cli;

import javax.swing.JFileChooser;

public class FolderChooser {

    public static String selecionarPasta(String mensagemTitulo) {
        // Cria a janela de seleção
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(mensagemTitulo);
        
        // Configura para selecionar APENAS pastas (ignora arquivos soltos)
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Exibe a janela e captura o botão que o usuário clicou (Abrir ou Cancelar)
        int retorno = chooser.showOpenDialog(null);

        // Se ele clicou em "Abrir", devolvemos o caminho bonitinho
        if (retorno == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }

        // Se ele fechou a janela ou cancelou, devolvemos null
        return null; 
    }
}