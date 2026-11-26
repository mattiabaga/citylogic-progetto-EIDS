
package com.citylogic;

import com.citylogic.controller.StatusDashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardPreview extends Application {

    // Valori utilizzati soltanto per provare la dashboard
    private double budget = 12500;
    private int population = 840;
    private double happiness = 74.5;
    private double pollution = 32.0;
    private long tick = 18;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/citylogic/view/status-dashboard.fxml")
        );

        Parent dashboard = loader.load();
        StatusDashboardController controller = loader.getController();

        refreshDashboard(controller);

        Button nextTick = new Button("Avanza tick");
        nextTick.setOnAction(event -> {
            tick++;
            population += 10;
            happiness += 0.5;
            pollution += 1;

            refreshDashboard(controller);
        });

        Button buildRoad = new Button("Costruisci strada (-500)");
        buildRoad.setOnAction(event -> {
            budget -= 500;
            refreshDashboard(controller);
        });

        VBox root = new VBox(12, dashboard, nextTick, buildRoad);
        root.setPadding(new Insets(15));

        stage.setTitle("Citylogic - Dashboard Preview");
        stage.setScene(new Scene(root, 340, 300));
        stage.show();
    }

    private void refreshDashboard(StatusDashboardController controller) {
        controller.updateMetrics(
                budget, population, happiness, pollution, tick
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
