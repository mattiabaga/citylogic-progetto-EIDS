package com.citylogic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * Controller del menù principale (main-menu.fxml).
 */
public class MainMenuController {

    @FXML
    private Label statusLabel;

    /** Pulsante "Nuova simulazione". */
    @FXML
    private void onNewSimulation() {
        // TODO: creare una nuova City e aprire la schermata di gioco
        statusLabel.setText("Nuova simulazione: la schermata di gioco non è ancora disponibile.");
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
