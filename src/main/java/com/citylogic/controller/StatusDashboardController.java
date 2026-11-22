
package com.citylogic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatusDashboardController {

    @FXML
    private Label budgetLabel;

    @FXML
    private Label populationLabel;

    @FXML
    private Label happinessLabel;

    @FXML
    private Label pollutionLabel;

    @FXML
    private Label tickLabel;

    public void updateMetrics(double budget, int population,
                              double happiness, double pollution,
                              long tick) {

        budgetLabel.setText(String.format("Budget: %,.0f", budget));
        populationLabel.setText("Popolazione: " + population);
        happinessLabel.setText(String.format("Felicità: %.1f%%", happiness));
        pollutionLabel.setText(String.format("Inquinamento: %.1f", pollution));
        tickLabel.setText("Tick: " + tick);

        // Evidenzia il budget negativo.
        budgetLabel.setStyle(
                budget < 0 ? "-fx-text-fill: #b91c1c;" : ""
        );
    }
}
