package com.intelli_file.presentation.gui;

import com.intelli_file.application.controller.FileController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
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

        listaRegras.setSelectionModel(new javafx.scene.control.MultipleSelectionModel<>() {
            @Override public ObservableList<Integer> getSelectedIndices() { return FXCollections.emptyObservableList(); }
            @Override public ObservableList<Map.Entry<String, List<String>>> getSelectedItems() { return FXCollections.emptyObservableList(); }
            @Override public void selectIndices(int i, int... ints) {}
            @Override public void selectAll() {}
            @Override public void selectFirst() {}
            @Override public void selectLast() {}
            @Override public void clearAndSelect(int i) {}
            @Override public void select(int i) {}
            @Override public void select(Map.Entry<String, List<String>> obj) {}
            @Override public void clearSelection(int i) {}
            @Override public void clearSelection() {}
            @Override public boolean isSelected(int i) { return false; }
            @Override public boolean isEmpty() { return true; }
            @Override public void selectPrevious() {}
            @Override public void selectNext() {}
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
        if (listaRegras != null) {
            listaRegras.getItems().clear();
            Map<String, List<String>> pastas = controller.getFoldersAndKeywords();
            listaRegras.getItems().addAll(pastas.entrySet());
        }
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

        try {
            controller.organizeSmart(pastaOrigem.getAbsolutePath(), pastaDestino.getAbsolutePath());

            Alert alertaSucesso = new Alert(Alert.AlertType.INFORMATION);
            alertaSucesso.setTitle("Operação Concluída");
            alertaSucesso.setHeaderText(null);
            alertaSucesso.setContentText("A organização dos arquivos foi finalizada com sucesso!");
            alertaSucesso.showAndWait();

        } catch (RuntimeException e) {
            Alert alertaErro = new Alert(Alert.AlertType.ERROR);
            alertaErro.setTitle("Erro na Organização");
            alertaErro.setHeaderText("Não foi possível organizar os arquivos.");
            alertaErro.setContentText(e.getMessage());
            alertaErro.showAndWait();
            e.printStackTrace();
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
}