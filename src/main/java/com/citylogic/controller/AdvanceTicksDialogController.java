package com.citylogic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Controller della finestra "Avanza nel tempo" (advance-ticks-dialog.fxml).
 *
 * <p>Raccoglie la scelta dell'utente e la converte in un unico numero:
 * quanti tick far avanzare la simulazione. Non esegue alcuna logica di
 * simulazione: il risultato viene letto da chi ha aperto la finestra
 * tramite {@link #getTicksToAdvance()}.</p>
 */
public class AdvanceTicksDialogController {

    /** Numero massimo di tick che si possono avanzare in un colpo solo. */
    public static final int MAX_TICKS_PER_STEP = 1000;

    @FXML private Label currentTickLabel;
    @FXML private ToggleGroup modeGroup;
    @FXML private RadioButton byAmountRadio;
    @FXML private RadioButton untilTickRadio;
    @FXML private TextField nField;
    @FXML private Label errorLabel;

    /** Tick in cui si trova ora la simulazione. */
    private int currentTick;

    /** Risultato: tick da avanzare, oppure null se l'utente ha annullato. */
    private Integer ticksToAdvance;

    /** Chiamato automaticamente da JavaFX dopo aver caricato l'FXML. */
    @FXML
    private void initialize() {
        // Il campo N accetta solo cifre (al massimo 6)
        nField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("\\d{0,6}") ? change : null));

        // Quando l'utente modifica qualcosa, si nasconde l'errore precedente
        nField.textProperty().addListener((obs, oldV, newV) -> clearError());
        modeGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> clearError());
    }

    /** Imposta il tick attuale (va chiamato prima di mostrare la finestra). */
    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
        currentTickLabel.setText("Tick attuale: " + currentTick);
    }

    /** @return quanti tick avanzare, oppure null se la finestra è stata annullata. */
    public Integer getTicksToAdvance() {
        return ticksToAdvance;
    }

    /** Pulsante "Avanza" (o Invio): valida il valore e chiude la finestra. */
    @FXML
    private void onConfirm() {
        String text = nField.getText();
        if (text.isEmpty()) {
            showError("Inserisci un numero.");
            return;
        }
        int n = Integer.parseInt(text); // il TextFormatter garantisce solo cifre

        int ticks;
        if (untilTickRadio.isSelected()) {
            if (n <= currentTick) {
                showError("N deve essere maggiore del tick attuale (" + currentTick + ").");
                return;
            }
            ticks = n - currentTick;
        } else {
            if (n < 1) {
                showError("N deve essere almeno 1.");
                return;
            }
            ticks = n;
        }

        if (ticks > MAX_TICKS_PER_STEP) {
            showError("Si possono avanzare al massimo " + MAX_TICKS_PER_STEP + " tick alla volta.");
            return;
        }

        ticksToAdvance = ticks;
        close();
    }

    /** Pulsante "Annulla" (o Esc): chiude senza fare nulla. */
    @FXML
    private void onCancel() {
        ticksToAdvance = null;
        close();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        if (!nField.getStyleClass().contains("invalid")) {
            nField.getStyleClass().add("invalid");
        }
        nField.requestFocus();
    }

    private void clearError() {
        errorLabel.setText("");
        nField.getStyleClass().remove("invalid");
    }

    private void close() {
        ((Stage) nField.getScene().getWindow()).close();
    }
}
