package com.citylogic.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Punto d'avvio dell'interfaccia JavaFX di CityLogic.
 * Carica il menù principale e mostra la finestra.
 */
public class CityLogicApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CityLogicApplication.class.getResource("/com/citylogic/view/main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("CityLogic");

        // Icona della finestra: più dimensioni, il sistema operativo sceglie la più adatta
        for (int size : new int[]{16, 32, 48, 64, 128, 256}) {
            stage.getIcons().add(new Image(Objects.requireNonNull(
                    CityLogicApplication.class.getResourceAsStream("/com/citylogic/view/images/icon-" + size + ".png"))));
        }

        stage.setMinWidth(600);
        stage.setMinHeight(450);
        stage.setScene(scene);
        stage.show();
    }
}
