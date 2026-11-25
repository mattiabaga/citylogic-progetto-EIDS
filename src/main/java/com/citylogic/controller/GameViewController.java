package com.citylogic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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

    /** Zoom minimo e massimo della mappa (1 = griglia intera visibile). */
    private static final double MIN_ZOOM = 0.5;
    private static final double MAX_ZOOM = 8;

    /** Sensibilità della rotella: un "clic" (deltaY = 40) ingrandisce di circa il 10%. */
    private static final double WHEEL_ZOOM_SPEED = 0.0025;

    /** Quanti pixel di mappa devono restare sempre visibili durante lo spostamento. */
    private static final double MIN_VISIBLE = 60;
    /**
     * Edifici mostrati nel menù di costruzione.
     * TODO: sostituire con i valori dell'enum BuildingType del model quando sarà completo.
     */
    private static final String[] BUILDING_NAMES = {
            "Casa", "Fabbrica", "Negozio", "Centrale elettrica", "Parco", "Strada"
    };

    // ----- Barra degli status -----
    @FXML private Label budgetValue;
    @FXML private Label populationValue;
    @FXML private Label happinessValue;

    // ----- Avanzamento del tempo -----
    @FXML private Label tickValue;

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

    /** Ultima posizione del mouse durante lo spostamento della mappa. */
    private double panLastX;
    private double panLastY;

    /** true mentre l'utente trascina la mappa con il tasto destro. */
    private boolean panning;

    /**
     * Tick mostrato nella barra in alto.
     * TODO: segnaposto finché la UI non è collegata al model; il valore vero
     * arriverà dal City (campo tick) tramite l'Observer.
     */
    private int currentTick = 0;

    /** Chiamato automaticamente da JavaFX dopo aver caricato l'FXML. */
    @FXML
    private void initialize() {
        createGrid();
        createBuildingButtons();
        setupMapNavigation();
        // F11: passa da finestra a schermo intero e viceversa.
        // La scena esiste solo dopo che la vista è stata inserita nella finestra,
        // quindi il tasto si registra quando la scena diventa disponibile.
        gridHolder.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) {
                // Quando la finestra viene massimizzata/ripristinata o va a schermo
                // intero, la mappa torna inquadrata per intero alla nuova scala
                if (scene.getWindow() instanceof Stage stage) {
                    stage.maximizedProperty().addListener((o, wasMax, isMax) -> resetView());
                    stage.fullScreenProperty().addListener((o, wasFull, isFull) -> resetView());
                }
                scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.F11) {
                        Stage stage = (Stage) scene.getWindow();
                        stage.setFullScreen(!stage.isFullScreen());
                        event.consume();
                    }
                });
            }
        });

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
                cell.setOnMouseClicked(event -> {
                    // Solo tasto sinistro, e solo se il mouse non è stato trascinato
                    if (event.getButton() == MouseButton.PRIMARY && event.isStillSincePress()) {
                        onCellClicked(cx, cy);
                    }
                });

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
        clampPan();
    }

    // =====================================================================
    // Navigazione della mappa: zoom e spostamento
    // =====================================================================

    /**
     * Collega gli eventi di mouse e trackpad all'area della mappa.
     *
     * <p>Zoom e spostamento sono ottenuti con le trasformazioni del nodo
     * (scale e translate) applicate al GridPane: le caselle mantengono la
     * loro dimensione "di base" e viene ingrandita l'intera griglia.</p>
     */
    private void setupMapNavigation() {
        // La mappa ingrandita non deve uscire dall'area centrale
        // (altrimenti coprirebbe la barra degli status e quella dei pulsanti)
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(gridHolder.widthProperty());
        clip.heightProperty().bind(gridHolder.heightProperty());
        gridHolder.setClip(clip);

        // Rotella del mouse (e scorrimento a due dita su alcuni trackpad)
        gridHolder.setOnScroll(event -> {
            if (event.getDeltaY() != 0) {
                zoomAt(Math.exp(event.getDeltaY() * WHEEL_ZOOM_SPEED), event.getX(), event.getY());
            }
            event.consume();
        });

        // Gesto "pizzico" sul trackpad / touchscreen
        gridHolder.setOnZoom(event -> {
            zoomAt(event.getZoomFactor(), event.getX(), event.getY());
            event.consume();
        });

        // Spostamento tenendo premuto il tasto destro
        gridHolder.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                panning = true;
                panLastX = event.getX();
                panLastY = event.getY();
                gridHolder.setCursor(Cursor.CLOSED_HAND);
            }
        });
        gridHolder.setOnMouseDragged(event -> {
            if (panning) {
                cityGrid.setTranslateX(cityGrid.getTranslateX() + event.getX() - panLastX);
                cityGrid.setTranslateY(cityGrid.getTranslateY() + event.getY() - panLastY);
                panLastX = event.getX();
                panLastY = event.getY();
                clampPan();
            }
        });
        gridHolder.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                panning = false;
                gridHolder.setCursor(Cursor.DEFAULT);
            }
        });
    }

    /**
     * Ingrandisce (factor &gt; 1) o rimpicciolisce (factor &lt; 1) la mappa
     * mantenendo fermo il punto sotto al cursore (x, y), espresso nelle
     * coordinate di gridHolder.
     */
    private void zoomAt(double factor, double x, double y) {
        double oldScale = cityGrid.getScaleX();
        double newScale = Math.clamp(oldScale * factor, MIN_ZOOM, MAX_ZOOM);
        if (newScale == oldScale) {
            return;
        }

        // JavaFX scala il nodo attorno al suo centro: calcoliamo di quanto
        // spostarlo perché il punto sotto al cursore resti nella stessa posizione.
        Bounds bounds = cityGrid.getBoundsInParent();
        double centerX = bounds.getMinX() + bounds.getWidth() / 2;
        double centerY = bounds.getMinY() + bounds.getHeight() / 2;
        double ratio = 1 - newScale / oldScale;

        cityGrid.setScaleX(newScale);
        cityGrid.setScaleY(newScale);
        cityGrid.setTranslateX(cityGrid.getTranslateX() + (x - centerX) * ratio);
        cityGrid.setTranslateY(cityGrid.getTranslateY() + (y - centerY) * ratio);
        clampPan();
    }

    /** Riporta la mappa alla vista iniziale: zoom 1 (griglia intera) e centrata. */
    private void resetView() {
        cityGrid.setScaleX(1);
        cityGrid.setScaleY(1);
        cityGrid.setTranslateX(0);
        cityGrid.setTranslateY(0);
    }

    /**
     * Impedisce di trascinare la mappa completamente fuori dalla vista:
     * almeno MIN_VISIBLE pixel della griglia restano sempre sullo schermo.
     */
    private void clampPan() {
        double scale = cityGrid.getScaleX();
        double maxX = Math.max(0, (cityGrid.getWidth() * scale + gridHolder.getWidth()) / 2 - MIN_VISIBLE);
        double maxY = Math.max(0, (cityGrid.getHeight() * scale + gridHolder.getHeight()) / 2 - MIN_VISIBLE);
        cityGrid.setTranslateX(Math.clamp(cityGrid.getTranslateX(), -maxX, maxX));
        cityGrid.setTranslateY(Math.clamp(cityGrid.getTranslateY(), -maxY, maxY));
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
    // Avanzamento del tempo
    // =====================================================================

    /** Pulsante "+1 tick". */
    @FXML
    private void onNextTick() {
        advanceTicks(1);
    }

    /**
     * Pulsante "Avanza…": apre una piccola finestra modale, al centro dello
     * schermo, in cui scegliere se avanzare di N tick o fino al tick N.
     */
    @FXML
    private void onOpenAdvanceDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getResource("/com/citylogic/view/advance-ticks-dialog.fxml")));
            Parent content = loader.load();
            AdvanceTicksDialogController dialog = loader.getController();
            dialog.setCurrentTick(currentTick);

            Stage stage = new Stage();
            stage.setTitle("Avanza nel tempo");
            stage.initOwner(tickValue.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL); // blocca la finestra di gioco finché è aperta
            stage.setResizable(false);
            stage.setScene(new Scene(content));
            stage.setOnShown(event -> stage.centerOnScreen());
            stage.showAndWait(); // attende la chiusura della finestra

            Integer ticks = dialog.getTicksToAdvance();
            if (ticks != null) {
                advanceTicks(ticks);
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Impossibile aprire la finestra: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Fa avanzare la simulazione di n tick.
     * TODO: per ogni tick chiamare gameController.nextTick(); il nuovo valore
     * del tick e le metriche arriveranno poi dall'Observer. Per ora si
     * aggiorna solo il contatore mostrato.
     */
    private void advanceTicks(int n) {
        currentTick += n;
        showTick(currentTick);
    }

    // =====================================================================
    // Barra degli status
    // =====================================================================

    /** Aggiorna il "Tick attuale" mostrato in alto a destra. */
    public void showTick(int tick) {
        tickValue.setText(String.valueOf(tick));
    }

    /**
     * Aggiorna i valori mostrati nella barra in alto.
     * Pensato per essere chiamato dall'Observer della UI a ogni tick,
     * con i valori letti dal CityState (il tick si aggiorna con showTick).
     */
    public void showStatus(double budget, int population, double happiness) {
        budgetValue.setText(String.format("%,.0f €", budget));
        populationValue.setText(String.format("%,d", population));
        happinessValue.setText(String.format("%.0f%%", happiness));
    }
}
