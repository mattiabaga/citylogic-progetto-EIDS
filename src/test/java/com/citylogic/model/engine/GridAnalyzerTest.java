
package com.citylogic.model.engine;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.Cell;
import com.citylogic.model.core.CityGrid;
import com.citylogic.model.core.buildings.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GridAnalyzerTest {

    private final GridAnalyzer analyzer = new GridAnalyzer();

    private CityGrid createGrid(int width, int height, Building... buildings)
            throws Exception {

        CityGrid grid = new CityGrid();

        setField(grid, "width", width);
        setField(grid, "height", height);

        Cell[][] cells = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell();
            }
        }

        setField(grid, "cells", cells);

        int index = 0;

        for (Building building : buildings) {
            int x = index % width;
            int y = index / width;

            setField(cells[x][y], "building", building);
            index++;
        }

        return grid;
    }

    private void setField(Object target, String fieldName, Object value)
            throws Exception {

        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void analyzeRejectsNullGrid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> analyzer.analyze(null)
        );
    }

    @Test
    void analyzeEmptyGridReturnsZeroValues() throws Exception {
        GridSnapshot snapshot = analyzer.analyze(createGrid(2, 2));

        assertEquals(0.0, snapshot.getTotalPollutionProduced());
        assertEquals(0.0, snapshot.getTotalPollutionReduction());
        assertEquals(0, snapshot.getTotalHouseCapacity());
        assertEquals(0, snapshot.getActiveHouseCapacity());
        assertEquals(0.0, snapshot.getTotalMaintenance());
        assertEquals(0, snapshot.getTotalWorkerNeeded());
        assertEquals(0.0, snapshot.getTotalFactoryProduction());
        assertEquals(0.0, snapshot.getTotalShopIncomeBonus());
    }

    @Test
    void analyzeAggregatesBuildingValuesCorrectly() throws Exception {

        House house1 = new House("H1", 100, 2.0, 1.0, 50);
        house1.setHasEnergy(true);

        House house2 = new House("H2", 100, 3.0, 2.0, 30);

        FactoryBuilding factory =
                new FactoryBuilding("F1", 200, 5.0, 4.0, 20, 100.0);

        Shop shop =
                new Shop("S1", 150, 2.5, 1.5, 40.0);

        Park park =
                new Park("P1", 80, 1.0, 0.5, 10.0);

        GridSnapshot snapshot = analyzer.analyze(
                createGrid(3, 2, house1, house2, factory, shop, park)
        );

        assertEquals(9.0, snapshot.getTotalPollutionProduced(), 0.001);
        assertEquals(10.0, snapshot.getTotalPollutionReduction(), 0.001);

        assertEquals(80, snapshot.getTotalHouseCapacity());
        assertEquals(50, snapshot.getActiveHouseCapacity());

        assertEquals(13.5, snapshot.getTotalMaintenance(), 0.001);

        assertEquals(20, snapshot.getTotalWorkerNeeded());
        assertEquals(100.0, snapshot.getTotalFactoryProduction(), 0.001);

        assertEquals(40.0, snapshot.getTotalShopIncomeBonus(), 0.001);
    }

    @Test
    void analyzeCountsOnlyHousesWithEnergyAsActive() throws Exception {

        House house1 = new House("H1", 100, 1.0, 0.0, 40);
        House house2 = new House("H2", 100, 1.0, 0.0, 30);
        House house3 = new House("H3", 100, 1.0, 0.0, 20);

        house1.setHasEnergy(true);
        house3.setHasEnergy(true);

        GridSnapshot snapshot = analyzer.analyze(
                createGrid(3, 1, house1, house2, house3)
        );

        assertEquals(90, snapshot.getTotalHouseCapacity());
        assertEquals(60, snapshot.getActiveHouseCapacity());
    }

    @Test
    void analyzeRejectsUnsupportedBuildingType() throws Exception {

        Building unsupported = new Building(
                "UNKNOWN", 100, 1.0, 1.0
        ) {};

        CityGrid grid = createGrid(1, 1, unsupported);

        assertThrows(
                IllegalStateException.class,
                () -> analyzer.analyze(grid)
        );
    }
}
