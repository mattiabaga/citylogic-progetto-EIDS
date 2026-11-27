
package com.citylogic.controller;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller della dashboard delle metriche (status-dashboard.fxml).
 *
 * <p>Componente di sola lettura: riceve i valori dall'esterno (dalla schermata
 * di gioco, e in futuro dall'Observer collegato al CityState) e si limita a
 * mostrarli, formattati da {@link StatusDashboardFormatter}.</p>
 */
public class StatusDashboardController {

    /** Stato CSS ":negative", usato per colorare di rosso il budget sotto zero. */
    private static final PseudoClass NEGATIVE = PseudoClass.getPseudoClass("negative");

    @FXML
    private Label budgetLabel;

    @FXML
    private Label populationLabel;

    @FXML
    private Label happinessLabel;

    @FXML
    private Label tickLabel;

    /** Aggiorna tutte le metriche mostrate, compreso il tick. */
    public void updateMetrics(double budget, int population,
                              double happiness, long tick) {

        budgetLabel.setText(StatusDashboardFormatter.budget(budget));
        populationLabel.setText(StatusDashboardFormatter.population(population));
        happinessLabel.setText(StatusDashboardFormatter.happiness(happiness));
        updateTick(tick);

        // Evidenzia il budget negativo (il colore è definito in style.css).
        budgetLabel.pseudoClassStateChanged(NEGATIVE, budget < 0);
    }

    /** Aggiorna solo il tick (es. dopo "+1 tick", prima che arrivino le nuove metriche). */
    public void updateTick(long tick) {
        tickLabel.setText(StatusDashboardFormatter.tick(tick));
    }
}
