
package com.citylogic.model.core;

import com.citylogic.model.core.buildings.House;
import com.citylogic.model.core.buildings.PowerPlant;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CityBuildingCoverageTest {

    @Test
    void placingPowerPlantUpdatesCoverageOfExistingHouses()
            throws Exception {

        City city = new City();
        TestCityGrid grid = new TestCityGrid(5, 5);

        House house = new House("H1", 0, 0, 0, 30);
        grid.seedBuilding(house, 2, 2);

        setField(city, "grid", grid);

        PowerPlant plant =
                new PowerPlant("P1", 0, 0, 0, 0, 1);

        city.placeBuilding(plant, 1, 2);

        assertTrue(house.hasEnergy());
    }

    @Test
    void removingPowerPlantUpdatesCoverageOfExistingHouses()
            throws Exception {

        City city = new City();
        TestCityGrid grid = new TestCityGrid(5, 5);

        House house = new House("H1", 0, 0, 0, 30);
        PowerPlant plant =
                new PowerPlant("P1", 0, 0, 0, 0, 1);

        grid.seedBuilding(house, 2, 2);
        grid.seedBuilding(plant, 1, 2);

        // Simula la copertura precedente alla rimozione.
        house.setHasEnergy(true);

        setField(city, "grid", grid);

        Building removed = city.removeBuilding(1, 2);

        assertSame(plant, removed);
        assertFalse(house.hasEnergy());
    }

    @Test
    void removingBuildingFromEmptyCellIsRejected() throws Exception {

        City city = new City();
        TestCityGrid grid = new TestCityGrid(5, 5);

        setField(city, "grid", grid);

        assertThrows(
                IllegalStateException.class,
                () -> city.removeBuilding(1, 1)
        );
    }

    @Test
    void placingNullBuildingIsRejected() {

        City city = new City();

        assertThrows(
                IllegalArgumentException.class,
                () -> city.placeBuilding(null, 1, 1)
        );
    }

    private static void setField(
            Object target, String fieldName, Object value)
            throws Exception {

        Field field = target.getClass()
                .getDeclaredField(fieldName);

        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * Griglia fittizia utilizzata esclusivamente per testare City.
     * Non verifica l'implementazione reale di CityGrid o Cell.
     */
    private static class TestCityGrid extends CityGrid {

        private final int width;
        private final int height;
        private final Cell[][] cells;

        TestCityGrid(int width, int height) throws Exception {
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
                throw new IllegalArgumentException(
                        "Invalid cell coordinates"
                );
            }

            return cells[x][y];
        }

        @Override
        public void placeBuilding(Building building, int x, int y) {
            Cell cell = getCell(x, y);

            if (building == null) {
                throw new IllegalArgumentException(
                        "Building cannot be null"
                );
            }

            if (cell.isOccupied()) {
                throw new IllegalStateException(
                        "Cell is already occupied"
                );
            }

            try {
                setField(cell, "building", building);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void removeBuilding(Building building, int x, int y) {
            Cell cell = getCell(x, y);

            if (cell.getBuilding() != building) {
                throw new IllegalStateException(
                        "Building not found at the specified position"
                );
            }

            try {
                setField(cell, "building", null);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        void seedBuilding(Building building, int x, int y) {
            placeBuilding(building, x, y);
        }
    }
}
