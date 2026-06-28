package com.intelli_file;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage palcoPrincipal) throws Exception {
        URL arquivoFXML = getClass().getResource("/views/MainView.fxml");
        FXMLLoader carregador = new FXMLLoader(arquivoFXML);
        
        // Criamos a cena inicial
        Scene cena = new Scene(carregador.load(), 1200, 800);
        
        // CARREGA O NOSSO ARQUIVO CSS GLOBAL
        String css = getClass().getResource("/styles/theme.css").toExternalForm();
        cena.getStylesheets().add(css);
        
        palcoPrincipal.setTitle("IntelliFiles");
        palcoPrincipal.setScene(cena);
        palcoPrincipal.setMaximized(true);
        palcoPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}