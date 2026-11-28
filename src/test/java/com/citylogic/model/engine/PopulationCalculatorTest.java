package com.citylogic.model.engine;

import com.citylogic.model.core.CityState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PopulationCalculatorTest {

    private final PopulationCalculator calculator = new PopulationCalculator();

    private GridSnapshot snapshot(int activeHouseCapacity) {
        return new GridSnapshot(
                0, 0,
                activeHouseCapacity,
                activeHouseCapacity,
                0, 0, 0, 0, 0
        );
    }

    @Test
    void populationGrowsWhenHappinessIsAboveThreshold() {
        GridSnapshot snapshot = snapshot(100);
        CityState state = new CityState(50, 50, 0, 0, 0);

        // delta = (100 - 50) * 0.5 * (50 - 30) / 100 = 5
        assertEquals(55, calculator.calculate(snapshot, state));
    }

    @Test
    void populationDoesNotChangeAtMinimumHappinessThreshold() {
        GridSnapshot snapshot = snapshot(100);
        CityState state = new CityState(50, 30, 0, 0, 0);

        assertEquals(50, calculator.calculate(snapshot, state));
    }

    @Test
    void populationDeclinesWhenHappinessIsBelowThreshold() {
        GridSnapshot snapshot = snapshot(100);
        CityState state = new CityState(50, 20, 0, 0, 0);

        // delta = -2.5; con Math.ceil il decremento applicato è 2.
        assertEquals(48, calculator.calculate(snapshot, state));
    }

    @Test
    void populationCannotExceedActiveHousingCapacity() {
        GridSnapshot snapshot = snapshot(100);
        CityState state = new CityState(100, 100, 0, 0, 0);

        assertEquals(100, calculator.calculate(snapshot, state));
    }

    @Test
    void throwsWhenCurrentPopulationExceedsActiveHousingCapacity() {
        GridSnapshot snapshot = snapshot(40);
        CityState state = new CityState(50, 50, 0, 0, 0);

        assertThrows(IllegalStateException.class,
                () -> calculator.calculate(snapshot, state));
    }

    @Test
    void zeroPopulationAndZeroCapacityRemainValid() {
        GridSnapshot snapshot = snapshot(0);
        CityState state = new CityState(0, 50, 0, 0, 0);

        assertEquals(0, calculator.calculate(snapshot, state));
    }
}
