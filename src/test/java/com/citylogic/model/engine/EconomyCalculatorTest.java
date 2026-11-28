package com.citylogic.model.engine;

import com.citylogic.model.core.CityState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EconomyCalculatorTest {

    private final EconomyCalculator calculator = new EconomyCalculator();

    private GridSnapshot snapshot(double maintenance,
                                  int workersNeeded,
                                  double factoryProduction,
                                  double shopIncomeBonus) {
        return new GridSnapshot(
                0, 0, 0, 0,
                maintenance,
                workersNeeded,
                factoryProduction,
                0,
                shopIncomeBonus
        );
    }

    @Test
    void factoryProductionIsFullyCountedWhenNoWorkersAreRequired() {
        GridSnapshot snapshot = snapshot(10, 0, 100, 20);
        CityState state = new CityState(0, 50, 0, 0, 0);

        // 100 produzione + 10 bonus negozi - 10 manutenzione
        assertEquals(100.0, calculator.calculate(snapshot, state), 1e-9);
    }

    @Test
    void factoryProductionIsProportionalToAvailablePopulation() {
        GridSnapshot snapshot = snapshot(10, 100, 200, 0);
        CityState state = new CityState(50, 0, 0, 0, 0);

        // 200 * (50 / 100) - 10 manutenzione
        assertEquals(90.0, calculator.calculate(snapshot, state), 1e-9);
    }

    @Test
    void shopBonusDependsOnHappinessAndMaintenanceIsSubtracted() {
        GridSnapshot snapshot = snapshot(5, 0, 0, 40);
        CityState state = new CityState(0, 75, 0, 0, 0);

        // 40 * 75% - 5 manutenzione
        assertEquals(25.0, calculator.calculate(snapshot, state), 1e-9);
    }

    @Test
    void rejectsNullSnapshot() {
        CityState state = new CityState();
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(null, state));
    }

    @Test
    void rejectsNullCityState() {
        GridSnapshot snapshot = snapshot(0, 0, 0, 0);
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(snapshot, null));
    }
}
