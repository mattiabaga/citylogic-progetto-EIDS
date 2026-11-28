package com.citylogic.controller;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.Cell;
import com.citylogic.model.core.City;
import com.citylogic.model.core.CityGrid;
import com.citylogic.model.core.buildings.House;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class GameViewControllerObserverTest {

    private static final int TIMEOUT_SECONDS = 10;

    @BeforeAll
    static void startJavaFxToolkit() throws Exception {
        CountDownLatch started = new CountDownLatch(1);

        try {
            Platform.startup(started::countDown);
        } catch (IllegalStateException alreadyStarted) {
            // Il toolkit JavaFX è già stato avviato da un altro test.
            started.countDown();
        }

        assertTrue(started.await(TIMEOUT_SECONDS, TimeUnit.SECONDS),
                "JavaFX Toolkit non si è avviato in tempo");
        Platform.setImplicitExit(false);
    }

    @Test
    void cityNotificationUpdatesHouseStyleAndDashboard() throws Exception {
        TestContext context = onFxThread(() -> createContext(true));

        onFxThread(() -> {
            context.city.applyTickResults(24, 82.5, 11.0, 0.0, 1250.5);

            assertTrue(context.visualHouseCell.getStyleClass().contains("house"));
            assertTrue(context.visualHouseCell.getStyleClass().contains("house-powered"));
            assertFalse(context.visualHouseCell.getStyleClass().contains("house-unpowered"));

            assertEquals("1.250,50 crediti", context.budgetLabel.getText());
            assertEquals("24", context.populationLabel.getText());
            assertEquals("82,5%", context.happinessLabel.getText());
            assertEquals("1", context.tickLabel.getText());
            return null;
        });
    }

    @Test
    void nextNotificationChangesHouseFromPoweredToUnpowered() throws Exception {
        TestContext context = onFxThread(() -> createContext(true));

        onFxThread(() -> {
            // Prima notifica: la casa risulta alimentata.
            context.city.applyTickResults(20, 75.0, 5.0, 0.0, 0.0);
            assertTrue(context.visualHouseCell.getStyleClass().contains("house-powered"));

            // Simula il nuovo stato calcolato da EnergyCoverageManager.
            context.house.setHasEnergy(false);
            context.city.applyTickResults(20, 75.0, 5.0, 0.0, 0.0);

            assertTrue(context.visualHouseCell.getStyleClass().contains("house"));
            assertTrue(context.visualHouseCell.getStyleClass().contains("house-unpowered"));
            assertFalse(context.visualHouseCell.getStyleClass().contains("house-powered"));
            assertEquals("2", context.tickLabel.getText());
            return null;
        });
    }

    @Test
    void notificationFromBackgroundThreadUpdatesGridAndDashboardOnFxThread()
            throws Exception {
        TestContext context = onFxThread(() -> createContext(false));

        AtomicReference<Boolean> dashboardUpdatedOnFxThread = new AtomicReference<>();
        AtomicReference<Boolean> gridUpdatedOnFxThread = new AtomicReference<>();

        onFxThread(() -> {
            context.budgetLabel.textProperty().addListener(
                    (observable, oldValue, newValue) ->
                            dashboardUpdatedOnFxThread.set(Platform.isFxApplicationThread()));

            context.visualHouseCell.getStyleClass().addListener(
                    (javafx.collections.ListChangeListener<String>) change ->
                            gridUpdatedOnFxThread.set(Platform.isFxApplicationThread()));
            return null;
        });

        Thread notifier = new Thread(
                () -> context.city.applyTickResults(15, 70.0, 3.0, 0.0, 500.0),
                "city-observer-test"
        );
        notifier.start();
        notifier.join(TIMEOUT_SECONDS * 1000L);

        assertFalse(notifier.isAlive(), "Il thread di notifica non è terminato in tempo");

        // Barriera: attende che siano eseguite le operazioni Platform.runLater
        // accodate dalla notifica partita dal thread secondario.
        onFxThread(() -> null);

        assertEquals(Boolean.TRUE, dashboardUpdatedOnFxThread.get(),
                "La Dashboard deve essere aggiornata sul JavaFX Application Thread");
        assertEquals(Boolean.TRUE, gridUpdatedOnFxThread.get(),
                "La griglia deve essere aggiornata sul JavaFX Application Thread");

        onFxThread(() -> {
            assertEquals("500,00 crediti", context.budgetLabel.getText());
            assertEquals("15", context.populationLabel.getText());
            assertEquals("70,0%", context.happinessLabel.getText());
            assertEquals("1", context.tickLabel.getText());
            assertTrue(context.visualHouseCell.getStyleClass().contains("house-unpowered"));
            return null;
        });
    }

    private static TestContext createContext(boolean housePowered) throws Exception {
        City city = new City();
        TestCityGrid grid = new TestCityGrid(2, 2);

        House house = new House("H1", 0, 0.0, 0.0, 30);
        house.setHasEnergy(housePowered);
        grid.putBuilding(house, 0, 0);
        setField(city, "grid", grid);

        GameViewController viewController = new GameViewController();
        StatusDashboardController dashboardController = new StatusDashboardController();

        Label budgetLabel = new Label();
        Label populationLabel = new Label();
        Label happinessLabel = new Label();
        Label tickLabel = new Label();

        setField(dashboardController, "budgetLabel", budgetLabel);
        setField(dashboardController, "populationLabel", populationLabel);
        setField(dashboardController, "happinessLabel", happinessLabel);
        setField(dashboardController, "tickLabel", tickLabel);
        setField(viewController, "statusDashboardController", dashboardController);

        Pane[][] visualCells = (Pane[][]) getField(viewController, "cells");
        for (int x = 0; x < visualCells.length; x++) {
            for (int y = 0; y < visualCells[x].length; y++) {
                visualCells[x][y] = new Pane();
                visualCells[x][y].getStyleClass().add("grid-cell");
            }
        }

        city.addObserver(viewController);

        return new TestContext(
                city,
                house,
                viewController,
                visualCells[0][0],
                budgetLabel,
                populationLabel,
                happinessLabel,
                tickLabel
        );
    }

    private static void setField(Object target, String fieldName, Object value)
            throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static Object getField(Object target, String fieldName)
            throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private static <T> T onFxThread(Callable<T> action) throws Exception {
        if (Platform.isFxApplicationThread()) {
            return action.call();
        }

        FutureTask<T> task = new FutureTask<>(action);
        Platform.runLater(task);
        return task.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    /** CityGrid di test: espone soltanto le celle necessarie a refreshGrid(). */
    private static final class TestCityGrid extends CityGrid {
        private final int width;
        private final int height;
        private final Cell[][] cells;

        private TestCityGrid(int width, int height) throws Exception {
            this.width = width;
            this.height = height;
            this.cells = new Cell[width][height];

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Cell cell = new Cell();
                    setField(cell, "x", x);
                    setField(cell, "y", y);
                    cells[x][y] = cell;
                }
            }
        }

        private void putBuilding(Building building, int x, int y) throws Exception {
            setField(getCell(x, y), "building", building);
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public Cell getCell(int x, int y) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                throw new IllegalArgumentException("Invalid cell coordinates");
            }
            return cells[x][y];
        }
    }

    private static final class TestContext {
        private final City city;
        private final House house;
        @SuppressWarnings("unused")
        private final GameViewController viewController;
        private final Pane visualHouseCell;
        private final Label budgetLabel;
        private final Label populationLabel;
        private final Label happinessLabel;
        private final Label tickLabel;

        private TestContext(City city,
                            House house,
                            GameViewController viewController,
                            Pane visualHouseCell,
                            Label budgetLabel,
                            Label populationLabel,
                            Label happinessLabel,
                            Label tickLabel) {
            this.city = city;
            this.house = house;
            this.viewController = viewController;
            this.visualHouseCell = visualHouseCell;
            this.budgetLabel = budgetLabel;
            this.populationLabel = populationLabel;
            this.happinessLabel = happinessLabel;
            this.tickLabel = tickLabel;
        }
    }
}
