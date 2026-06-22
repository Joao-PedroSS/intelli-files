package com.intelli_file.presentation.cli;

import javax.swing.JFileChooser;
import javax.swing.UIManager; // <-- Importação nova aqui!

public class FolderChooser {

    public static String selecionarPasta(String mensagemTitulo) {
        
        // 1. Força o Java a imitar o visual do Windows (Look and Feel)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se o Java não conseguir imitar o Windows por algum motivo, 
            // ele apenas ignora o erro e abre com o visual antigo sem travar o sistema.
        }

        // 2. Cria a janela de seleção (agora com a roupagem nova)
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(mensagemTitulo);
        
        // 3. Configura para selecionar APENAS pastas
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // 4. Exibe a janela e captura o botão que o usuário clicou
        int retorno = chooser.showOpenDialog(null);

        // 5. Se ele clicou em "Abrir", devolvemos o caminho
        if (retorno == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }

        // Se ele fechou a janela ou cancelou, devolvemos null
        return null; 
    }
}