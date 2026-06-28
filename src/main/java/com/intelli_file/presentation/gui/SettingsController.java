package com.intelli_file.presentation.gui;

import com.intelli_file.application.controller.FileController;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SettingsController {

    @FXML private VBox painelPastas;
    @FXML private VBox painelPalavras;

    @FXML private Button btnMenuPastas;
    @FXML private Button btnMenuPalavras;

    @FXML private TextField campoNomePasta;
    @FXML private TextField campoPastaAlvo;
    @FXML private TextField campoNovaKeyword;

    @FXML private Label labelStatus;
    @FXML private ListView<Map.Entry<String, List<String>>> listaRegrasConfig;

    private final FileController controller = new FileController();

    @FXML
    public void initialize() {
        configurarAparenciaDaLista(); // estava faltando esta linha
        carregarRegrasNaListaVisual();
        mostrarMenuPastas();

        listaRegrasConfig.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) listaRegrasConfig.getSelectionModel().clearSelection();
        });
    }

    private void configurarAparenciaDaLista() {
        Label avisoVazio = new Label("Seu sistema está zerado! Use os menus acima para criar a sua primeira regra.");
        avisoVazio.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 16px; -fx-font-style: italic;");
        listaRegrasConfig.setPlaceholder(avisoVazio);

        // CORREÇÃO: O tipo da ListCell deve bater exatamente com o tipo da ListView
        listaRegrasConfig.setCellFactory(lista -> new ListCell<Map.Entry<String, List<String>>>() {
            @Override
            protected void updateItem(Map.Entry<String, List<String>> regra, boolean vazio) {
                super.updateItem(regra, vazio);

                if (vazio || regra == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    HBox linha = new HBox(15);
                    linha.setStyle("-fx-padding: 12px; -fx-background-color: #2d3139; -fx-background-radius: 8px;");
                    linha.setAlignment(Pos.CENTER_LEFT);

                    Label labelPasta = new Label("Pasta: " + regra.getKey());
                    labelPasta.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 15px;");

                    Label labelSeta = new Label("➔");
                    labelSeta.setStyle("-fx-text-fill: #4f46e5; -fx-font-weight: bold; -fx-font-size: 16px;");

                    Label labelPalavras = new Label(String.join(", ", regra.getValue()));
                    labelPalavras.setStyle("-fx-text-fill: #10b981; -fx-font-size: 14px; -fx-font-weight: bold;");

                    linha.getChildren().addAll(labelPasta, labelSeta, labelPalavras);
                    setGraphic(linha);
                    setText(null);
                    setStyle("-fx-background-color: transparent; -fx-padding: 5px;");
                }
            }
        });
    }

    @FXML
    public void mostrarMenuPastas() {
        painelPastas.setVisible(true);
        painelPastas.setManaged(true);
        painelPalavras.setVisible(false);
        painelPalavras.setManaged(false);
        labelStatus.setText("");
        
        // MÁGICA VISUAL: Pinta o botão de Pastas de azul e o outro de cinza
        if (btnMenuPastas != null && btnMenuPalavras != null) {
            btnMenuPastas.getStyleClass().setAll("button", "btn-menu-active");
            btnMenuPalavras.getStyleClass().setAll("button", "btn-menu");
        }
    }

    @FXML
    public void mostrarMenuPalavras() {
        painelPalavras.setVisible(true);
        painelPalavras.setManaged(true);
        painelPastas.setVisible(false);
        painelPastas.setManaged(false);
        labelStatus.setText("");
        
        // MÁGICA VISUAL: Pinta o botão de Palavras de azul e o outro de cinza
        if (btnMenuPastas != null && btnMenuPalavras != null) {
            btnMenuPalavras.getStyleClass().setAll("button", "btn-menu-active");
            btnMenuPastas.getStyleClass().setAll("button", "btn-menu");
        }
    }

    private void carregarRegrasNaListaVisual() {
        listaRegrasConfig.getItems().clear();
        Map<String, List<String>> pastas = controller.getFoldersAndKeywords();

        // Em vez de String, adiciona o Map.Entry diretamente — bate com ListView<Map.Entry<...>>
        for (Map.Entry<String, List<String>> entry : pastas.entrySet()) {
            listaRegrasConfig.getItems().add(entry);
        }
    }

    private String buscarPastaIgnorandoCaixa(Map<String, List<String>> regras, String pastaBuscada) {
        for (String chaveExistente : regras.keySet()) {
            if (chaveExistente.equalsIgnoreCase(pastaBuscada)) {
                return chaveExistente;
            }
        }
        return null;
    }

    private String buscarPalavraIgnorandoCaixa(List<String> palavras, String palavraBuscada) {
        for (String palavraExistente : palavras) {
            if (palavraExistente.equalsIgnoreCase(palavraBuscada)) {
                return palavraExistente;
            }
        }
        return null;
    }

    // =========================================================
    // PASTAS
    // =========================================================

    @FXML
    public void adicionarPasta(ActionEvent event) {
        String pasta = campoNomePasta.getText();
        if (pasta != null && !pasta.trim().isEmpty()) {
            pasta = pasta.trim();
            Map<String, List<String>> regrasAtuais = controller.getFoldersAndKeywords();
            
            if (buscarPastaIgnorandoCaixa(regrasAtuais, pasta) != null) {
                mostrarMensagemErro("A pasta já existe no sistema!");
            } else {
                controller.addFolderConfig(pasta);
                mostrarMensagemSucesso("Pasta adicionada com sucesso!");
                campoNomePasta.clear();
                carregarRegrasNaListaVisual(); 
            }
        } else {
            mostrarMensagemErro("Digite um nome válido.");
        }
    }

    @FXML
    public void removerPasta(ActionEvent event) {
        String pasta = campoNomePasta.getText();
        if (pasta != null && !pasta.trim().isEmpty()) {
            pasta = pasta.trim();
            Map<String, List<String>> regrasAtuais = controller.getFoldersAndKeywords();
            String pastaExistente = buscarPastaIgnorandoCaixa(regrasAtuais, pasta);
            
            if (pastaExistente == null) {
                mostrarMensagemErro("A pasta não existe.");
            } else {
                // EXIBE A CAIXA DE CONFIRMAÇÃO
                if (confirmarAcao("Tem certeza que deseja apagar a pasta '" + pastaExistente + "' e todas as suas regras?")) {
                    controller.removeFolderConfig(pastaExistente);
                    mostrarMensagemSucesso("Pasta removida com sucesso!");
                    campoNomePasta.clear();
                    carregarRegrasNaListaVisual();
                }
            }
        } else {
            mostrarMensagemErro("Digite o nome da pasta.");
        }
    }

    // =========================================================
    // PALAVRAS-CHAVE
    // =========================================================

    @FXML
    public void adicionarKeyword(ActionEvent event) {
        String pasta = campoPastaAlvo.getText();
        String palavra = campoNovaKeyword.getText();
        
        if (pasta != null && !pasta.trim().isEmpty() && palavra != null && !palavra.trim().isEmpty()) {
            pasta = pasta.trim();
            palavra = palavra.trim();
            Map<String, List<String>> regrasAtuais = controller.getFoldersAndKeywords();
            String pastaExistente = buscarPastaIgnorandoCaixa(regrasAtuais, pasta);
            
            if (pastaExistente == null) {
                mostrarMensagemErro("A pasta não existe.");
            } else {
                if (buscarPalavraIgnorandoCaixa(regrasAtuais.get(pastaExistente), palavra) != null) {
                    mostrarMensagemErro("A palavra já existe nesta pasta.");
                } else {
                    controller.addKeywordConfig(pastaExistente, palavra);
                    mostrarMensagemSucesso("Palavra adicionada!");
                    campoNovaKeyword.clear();
                    carregarRegrasNaListaVisual(); 
                }
            }
        } else {
            mostrarMensagemErro("Preencha a pasta e a palavra.");
        }
    }

    @FXML
    public void removerKeyword(ActionEvent event) {
        String pasta = campoPastaAlvo.getText();
        String palavra = campoNovaKeyword.getText();
        
        if (pasta != null && !pasta.trim().isEmpty() && palavra != null && !palavra.trim().isEmpty()) {
            pasta = pasta.trim();
            palavra = palavra.trim();
            Map<String, List<String>> regrasAtuais = controller.getFoldersAndKeywords();
            String pastaExistente = buscarPastaIgnorandoCaixa(regrasAtuais, pasta);
            
            if (pastaExistente == null) {
                mostrarMensagemErro("A pasta não existe.");
            } else {
                String palavraExistente = buscarPalavraIgnorandoCaixa(regrasAtuais.get(pastaExistente), palavra);
                if (palavraExistente == null) {
                    mostrarMensagemErro("A palavra não foi encontrada.");
                } else {
                    // EXIBE A CAIXA DE CONFIRMAÇÃO
                    if (confirmarAcao("Remover a palavra '" + palavraExistente + "' da pasta '" + pastaExistente + "'?")) {
                        controller.removeKeywordConfig(pastaExistente, palavraExistente);
                        mostrarMensagemSucesso("Palavra removida!");
                        campoNovaKeyword.clear();
                        carregarRegrasNaListaVisual();
                    }
                }
            }
        } else {
            mostrarMensagemErro("Preencha a pasta e a palavra.");
        }
    }

    // =========================================================
    // UTILITÁRIOS (Feedback e Navegação)
    // =========================================================

    private boolean confirmarAcao(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Exclusão");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    private void mostrarMensagemSucesso(String mensagem) {
        labelStatus.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold; -fx-font-size: 15px;");
        labelStatus.setText("Sucesso: " + mensagem);
        apagarMensagemAposDelay();
    }

    private void mostrarMensagemErro(String mensagem) {
        labelStatus.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold; -fx-font-size: 15px;");
        labelStatus.setText("Erro: " + mensagem);
        apagarMensagemAposDelay();
    }

    private void apagarMensagemAposDelay() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> labelStatus.setText(""));
        pause.play();
    }

    @FXML
    public void voltarParaInicio(ActionEvent event) {
        try {
            Parent painelPrincipal = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            Stage janelaAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            janelaAtual.getScene().setRoot(painelPrincipal);
        } catch (Exception e) {
            System.err.println("Erro ao voltar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}