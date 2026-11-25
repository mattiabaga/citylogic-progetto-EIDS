package com.citylogic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller del menù principale (main-menu.fxml).
 */
public class MainMenuController {

    @FXML
    private Label statusLabel;

    /** Pulsante "Nuova simulazione": apre la schermata di gioco. */
    @FXML
    private void onNewSimulation() {
        // TODO: chiedere al GameController di creare una nuova City
        try {
            Parent gameView = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/com/citylogic/view/game-view.fxml")));
            // Si sostituisce il contenuto della finestra, mantenendone le dimensioni
            statusLabel.getScene().setRoot(gameView);
        } catch (IOException e) {
            statusLabel.setText("Impossibile aprire la schermata di gioco: " + e.getMessage());
        }
    }

    /** Pulsante "Carica un salvataggio": apre la finestra di scelta del file. */
    @FXML
    private void onLoadSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Carica un salvataggio");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Salvataggi CityLogic (*.citylogic)", "*.citylogic"),
                new FileChooser.ExtensionFilter("Tutti i file", "*.*")
        );

        Window owner = statusLabel.getScene().getWindow();
        File file = chooser.showOpenDialog(owner);
        if (file == null) {
            return; // l'utente ha annullato
        }

        // TODO: gameController.load(file.getAbsolutePath()) e aprire la schermata di gioco
        statusLabel.setText("Salvataggio selezionato: " + file.getName());
    }
}
