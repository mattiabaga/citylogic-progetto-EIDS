package com.citylogic.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controller della schermata di gioco (game-view.fxml).
 *
 * <p>Si occupa solo della parte grafica: costruisce la griglia, apre e chiude
 * i menù e mostra i valori nella barra degli status. Tutta la logica della
 * simulazione resta nel model: questa classe dovrà solo chiamare il
 * GameController (costruzione, tick, policy) e ricevere i nuovi valori
 * tramite il pattern Observer.</p>
 */
public class GameViewController {

    /** Lato della griglia della città, in caselle (specifica: 20x20). */
    private static final int GRID_SIZE = 20;

    /** Spazio in pixel tra una casella e l'altra (forma le linee della griglia). */
    private static final double CELL_GAP = 1;

    /** Margine minimo tra la griglia e i bordi dell'area centrale. */
    private static final double GRID_MARGIN = 16;

    /**
     * Edifici mostrati nel menù di costruzione.
     * TODO: sostituire con i valori dell'enum BuildingType del model quando sarà completo.
     */
    private static final String[] BUILDING_NAMES = {
            "Casa", "Fabbrica", "Negozio", "Centrale elettrica", "Parco", "Strada"
    };

    // ----- Barra degli status -----
    @FXML private Label monthValue;
    @FXML private Label budgetValue;
    @FXML private Label populationValue;
    @FXML private Label happinessValue;
    @FXML private Label pollutionValue;
    @FXML private Label energyValue;

    // ----- Griglia -----
    @FXML private StackPane gridHolder;
    @FXML private GridPane cityGrid;

    // ----- Menù a comparsa -----
    @FXML private VBox buildPanel;
    @FXML private VBox chartsPanel;
    @FXML private FlowPane buildingButtons;
    @FXML private Label buildHint;
    @FXML private LineChart<Number, Number> metricsChart;

    // ----- Barra inferiore -----
    @FXML private ToggleButton buildToggle;
    @FXML private ToggleButton chartsToggle;

    /** Matrice delle caselle grafiche, indicizzata come cells[x][y]. */
    private final Pane[][] cells = new Pane[GRID_SIZE][GRID_SIZE];

    /** Gruppo dei pulsanti edificio: al massimo uno selezionato alla volta. */
    private final ToggleGroup buildingGroup = new ToggleGroup();

    /** Casella evidenziata dall'ultimo clic (null se nessuna). */
    private Pane selectedCell;

    /** Chiamato automaticamente da JavaFX dopo aver caricato l'FXML. */
    @FXML
    private void initialize() {
        createGrid();
        createBuildingButtons();

        // Le caselle si ridimensionano quando cambia lo spazio disponibile
        gridHolder.widthProperty().addListener((obs, oldV, newV) -> resizeCells());
        gridHolder.heightProperty().addListener((obs, oldV, newV) -> resizeCells());
    }

    // =====================================================================
    // Griglia
    // =====================================================================

    /** Crea le 20x20 caselle e le inserisce nel GridPane. */
    private void createGrid() {
        cityGrid.setHgap(CELL_GAP);
        cityGrid.setVgap(CELL_GAP);
        // La griglia non deve allargarsi oltre la sua dimensione preferita,
        // altrimenti lo StackPane la stirerebbe e le caselle non sarebbero quadrate
        cityGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Il contenitore può restringersi liberamente quando si rimpicciolisce la finestra
        gridHolder.setMinSize(0, 0);

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Pane cell = new Pane();
                cell.getStyleClass().add("grid-cell");

                final int cx = x;
                final int cy = y;
                cell.setOnMouseClicked(event -> onCellClicked(cx, cy));

                cells[x][y] = cell;
                cityGrid.add(cell, x, y); // colonna = x, riga = y
            }
        }
    }

    /**
     * Calcola il lato delle caselle in base allo spazio libero, usando il lato
     * più corto dell'area: così la griglia intera è quadrata e ci sta sempre.
     */
    private void resizeCells() {
        double available = Math.min(gridHolder.getWidth(), gridHolder.getHeight()) - 2 * GRID_MARGIN;
        double totalGaps = (GRID_SIZE - 1) * CELL_GAP + cityGrid.snappedLeftInset() + cityGrid.snappedRightInset();
        // floor: lati interi, così le linee della griglia restano nitide
        double side = Math.max(4, Math.floor((available - totalGaps) / GRID_SIZE));

        for (Pane[] column : cells) {
            for (Pane cell : column) {
                cell.setMinSize(side, side);
                cell.setPrefSize(side, side);
                cell.setMaxSize(side, side);
            }
        }
    }

    /** Gestisce il clic su una casella: per ora la evidenzia soltanto. */
    private void onCellClicked(int x, int y) {
        if (selectedCell != null) {
            selectedCell.getStyleClass().remove("selected");
        }
        selectedCell = cells[x][y];
        selectedCell.getStyleClass().add("selected");

        ToggleButton chosen = (ToggleButton) buildingGroup.getSelectedToggle();
        if (chosen != null) {
            // TODO: gameController.buildBuilding(tipo, x, y) e ridisegnare la casella
            buildHint.setText(chosen.getText() + " → casella (" + x + ", " + y + "): "
                    + "costruzione non ancora collegata al model.");
        }
    }

    // =====================================================================
    // Menù di costruzione e grafici
    // =====================================================================

    /** Crea un pulsante per ogni tipo di edificio nel menù di costruzione. */
    private void createBuildingButtons() {
        for (String name : BUILDING_NAMES) {
            ToggleButton button = new ToggleButton(name);
            button.setToggleGroup(buildingGroup);
            button.getStyleClass().add("building-button");
            buildingButtons.getChildren().add(button);
        }
    }

    /** Pulsante "Costruisci": apre/chiude il relativo menù. */
    @FXML
    private void onToggleBuildMenu() {
        showPanel(buildToggle.isSelected() ? buildPanel : null);
    }

    /** Pulsante "Grafici": apre/chiude il relativo menù. */
    @FXML
    private void onToggleChartsMenu() {
        showPanel(chartsToggle.isSelected() ? chartsPanel : null);
    }

    /**
     * Mostra al massimo un pannello alla volta: quello passato
     * (null = chiudi tutto) e allinea lo stato dei pulsanti in basso.
     */
    private void showPanel(VBox panel) {
        setPanelVisible(buildPanel, panel == buildPanel);
        setPanelVisible(chartsPanel, panel == chartsPanel);
        buildToggle.setSelected(panel == buildPanel);
        chartsToggle.setSelected(panel == chartsPanel);
    }

    private void setPanelVisible(VBox panel, boolean visible) {
        panel.setVisible(visible);
        panel.setManaged(visible);
    }

    // =====================================================================
    // Barra degli status
    // =====================================================================

    /**
     * Aggiorna i valori mostrati nella barra in alto.
     * Pensato per essere chiamato dall'Observer della UI a ogni tick,
     * con i valori letti dal CityState.
     */
    public void showStatus(int month, double budget, int population,
                           double happiness, double pollution, double energy) {
        monthValue.setText(String.valueOf(month));
        budgetValue.setText(String.format("%,.0f €", budget));
        populationValue.setText(String.format("%,d", population));
        happinessValue.setText(String.format("%.0f%%", happiness));
        pollutionValue.setText(String.format("%.0f%%", pollution));
        energyValue.setText(String.format("%,.0f", energy));
    }
}
