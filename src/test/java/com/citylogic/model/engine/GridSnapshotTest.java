package com.citylogic.model.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridSnapshotTest {

    private GridSnapshot createSnapshot(
            double pollutionProduced,
            double pollutionReduction,
            int houseCapacity,
            int activeHouseCapacity,
            double maintenance,
            int workersNeeded,
            double factoryProduction,
            double shopIncomeBonus) {

        return new GridSnapshot(
                pollutionProduced,
                pollutionReduction,
                houseCapacity,
                activeHouseCapacity,
                maintenance,
                workersNeeded,
                factoryProduction,
                0.0,
                shopIncomeBonus
        );
    }

    @Test
    void constructorStoresAllValuesCorrectly() {
        GridSnapshot snapshot = createSnapshot(
                12.5, 4.5, 100, 60, 25.0, 30, 150.0, 75.0
        );

        assertEquals(12.5, snapshot.getTotalPollutionProduced(), 0.001);
        assertEquals(4.5, snapshot.getTotalPollutionReduction(), 0.001);
        assertEquals(100, snapshot.getTotalHouseCapacity());
        assertEquals(60, snapshot.getActiveHouseCapacity());
        assertEquals(25.0, snapshot.getTotalMaintenance(), 0.001);
        assertEquals(30, snapshot.getTotalWorkerNeeded());
        assertEquals(150.0, snapshot.getTotalFactoryProduction(), 0.001);
        assertEquals(75.0, snapshot.getTotalShopIncomeBonus(), 0.001);
    }

    @Test
    void constructorAcceptsZeroValues() {
        GridSnapshot snapshot = createSnapshot(
                0.0, 0.0, 0, 0, 0.0, 0, 0.0, 0.0
        );

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
    void constructorRejectsNegativePollutionProduced() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(-1.0, 0, 0, 0, 0, 0, 0, 0));
    }

    @Test
    void constructorRejectsInvalidPollutionProduced() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(Double.NaN, 0, 0, 0, 0, 0, 0, 0));

        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(Double.POSITIVE_INFINITY, 0, 0, 0, 0, 0, 0, 0));
    }

    @Test
    void constructorRejectsNegativePollutionReduction() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, -1.0, 0, 0, 0, 0, 0, 0));
    }

    @Test
    void constructorRejectsNegativeHouseCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, -1, 0, 0, 0, 0, 0));
    }

    @Test
    void constructorRejectsInvalidActiveHouseCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 10, -1, 0, 0, 0, 0));

        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 10, 11, 0, 0, 0, 0));
    }

    @Test
    void constructorRejectsNegativeMaintenance() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, -1.0, 0, 0, 0));
    }

    @Test
    void constructorRejectsInvalidMaintenance() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, Double.NaN, 0, 0, 0));

        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, Double.POSITIVE_INFINITY, 0, 0, 0));
    }

    @Test
    void constructorRejectsNegativeWorkerNeeded() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, 0, -1, 0, 0));
    }

    @Test
    void constructorRejectsNegativeFactoryProduction() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, 0, 0, -1.0, 0));
    }

    @Test
    void constructorRejectsInvalidFactoryProduction() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, 0, 0, Double.NaN, 0));

        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, 0, 0, Double.POSITIVE_INFINITY, 0));
    }

    @Test
    void constructorRejectsNegativeShopIncomeBonus() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, 0, 0, 0, -1.0));
    }

    @Test
    void constructorRejectsInvalidShopIncomeBonus() {
        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, 0, 0, 0, Double.NaN));

        assertThrows(IllegalArgumentException.class,
                () -> createSnapshot(0, 0, 0, 0, 0, 0, 0, Double.POSITIVE_INFINITY));
    }
}