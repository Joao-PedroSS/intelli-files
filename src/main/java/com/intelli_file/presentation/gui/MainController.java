package com.intelli_file.presentation.gui;

import com.intelli_file.application.controller.FileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MainController {

    // 1. Mudamos o tipo da lista para receber o objeto puro em vez de texto solto
    @FXML private ListView<Map.Entry<String, List<String>>> listaRegras;
    
    private final FileController controller = new FileController();

    @FXML
    public void initialize() {
        configurarAparenciaDaLista();
        carregarRegrasNaListaVisual();

        listaRegras.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                listaRegras.getSelectionModel().clearSelection();
            }
        });
    }

    private void configurarAparenciaDaLista() {
        // O "Empty State": O que aparece quando o config.json está vazio
        Label avisoVazio = new Label("Nenhuma regra configurada. Clique em 'Gerenciar Regras e Pastas' para começar.");
        avisoVazio.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 16px; -fx-font-style: italic;");
        listaRegras.setPlaceholder(avisoVazio);

        // A Mágica do Layout: Desenhando como cada linha deve aparecer
        listaRegras.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(Map.Entry<String, List<String>> regra, boolean vazio) {
                super.updateItem(regra, vazio);

                if (vazio || regra == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    // Cria uma caixa horizontal para a linha
                    HBox linha = new HBox(15);
                    linha.setStyle("-fx-padding: 12px; -fx-background-color: #2d3139; -fx-background-radius: 8px;");
                    linha.setAlignment(Pos.CENTER_LEFT);

                    // Nome da Pasta (Destaque Branco)
                    Label labelPasta = new Label("Pasta: " + regra.getKey());
                    labelPasta.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 15px;");

                    // Setinha separadora (Destaque Azul)
                    Label labelSeta = new Label("➔");
                    labelSeta.setStyle("-fx-text-fill: #4f46e5; -fx-font-weight: bold; -fx-font-size: 16px;");

                    // Palavras-chave (Destaque Verde)
                    Label labelPalavras = new Label(String.join(", ", regra.getValue()));
                    labelPalavras.setStyle("-fx-text-fill: #10b981; -fx-font-size: 14px; -fx-font-weight: bold;");

                    // Junta tudo dentro da caixa e envia para a tela
                    linha.getChildren().addAll(labelPasta, labelSeta, labelPalavras);
                    setGraphic(linha);
                    setText(null);
                    setStyle("-fx-background-color: transparent; -fx-padding: 5px;"); // Espaçamento entre as linhas
                }
            }
        });
    }

    private void carregarRegrasNaListaVisual() {
        listaRegras.getItems().clear();
        Map<String, List<String>> pastas = controller.getFoldersAndKeywords();
        // Adicionamos os objetos diretamente na lista
        listaRegras.getItems().addAll(pastas.entrySet());
    }

    @FXML
    public void organizarArquivos(ActionEvent event) {
        Stage janelaAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DirectoryChooser seletor = new DirectoryChooser();
        
        seletor.setTitle("IntelliFiles: Selecione a pasta de ORIGEM");
        File pastaOrigem = seletor.showDialog(janelaAtual);
        if (pastaOrigem == null) return;

        seletor.setTitle("IntelliFiles: Selecione a pasta de DESTINO");
        File pastaDestino = seletor.showDialog(janelaAtual);
        if (pastaDestino == null) return;

        // O "Pulo do Gato": Vamos capturar qualquer erro que ocorra no processamento
        try {
            // Supondo que organizeSmart() retorne void ou lance exceção em caso de erro
            controller.organizeSmart(pastaOrigem.getAbsolutePath(), pastaDestino.getAbsolutePath());
            
            // Se chegou aqui sem erro, deu tudo certo!
            mostrarFeedback("Processo Concluído", 
                            "Arquivos organizados com sucesso na pasta de destino!", 
                            AlertType.INFORMATION);
                            
        } catch (Exception e) {
            // Se qualquer coisa falhar (acesso negado, disco cheio, erro no JSON), o usuário será avisado
            mostrarFeedback("Erro no Processamento", 
                            "Não foi possível organizar os arquivos:\n" + e.getMessage(), 
                            AlertType.ERROR);
            e.printStackTrace(); // Loga o erro no terminal para você debugar
        }
    }

    @FXML
    public void abrirConfiguracoes(ActionEvent event) {
        try {
            Parent painelConfiguracoes = FXMLLoader.load(getClass().getResource("/views/SettingsView.fxml"));
            Stage janelaAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene cenaAtual = janelaAtual.getScene();
            cenaAtual.setRoot(painelConfiguracoes);
        } catch (Exception e) {
            System.err.println("Erro ao mudar para a tela de configurações: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarFeedback(String titulo, String mensagem, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null); // Remove o cabeçalho padrão para ficar mais limpo
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}