package com.citylogic.model.engine;

import com.citylogic.model.core.CityState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HappinessCalculatorTest {

    private final HappinessCalculator calculator = new HappinessCalculator();

    private GridSnapshot snapshot(double pollutionProduced,
                                  double pollutionReduction,
                                  int activeHouseCapacity) {
        return new GridSnapshot(
                pollutionProduced,
                pollutionReduction,
                activeHouseCapacity,
                activeHouseCapacity,
                0,
                0,
                0,
                0,
                0
        );
    }

    @Test
    void emptyCityWithNoHousingCapacityDoesNotReceiveOvercrowdingPenalty() {
        GridSnapshot snapshot = snapshot(0, 0, 0);
        CityState state = new CityState(0, 50, 0, 0, 0);

        // Felicità attesa 100; adattamento del 20% da 50 a 100 = 60.
        assertEquals(60.0, calculator.calculate(snapshot, state), 1e-9);
    }

    @Test
    void calculatesOvercrowdingAndPollutionPenalties() {
        GridSnapshot snapshot = snapshot(10, 0, 100);
        CityState state = new CityState(100, 80, 0, 0, 0);

        // Penalità sovraffollamento 30, inquinamento 3:
        // felicità attesa 67; adattamento da 80 del 20% = 77.4.
        assertEquals(77.4, calculator.calculate(snapshot, state), 1e-9);
    }

    @Test
    void pollutionReductionCannotProduceNegativePollutionPenalty() {
        GridSnapshot snapshot = snapshot(5, 10, 100);
        CityState state = new CityState(0, 50, 0, 0, 0);

        // Inquinamento netto negativo: penalità nulla; felicità attesa 100.
        assertEquals(60.0, calculator.calculate(snapshot, state), 1e-9);
    }

    @Test
    void rejectsNullSnapshot() {
        CityState state = new CityState();
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(null, state));
    }

    @Test
    void rejectsNullCityState() {
        GridSnapshot snapshot = snapshot(0, 0, 0);
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(snapshot, null));
    }

    @Test
    void happinessCannotFallBelowZero() {
        GridSnapshot snapshot = snapshot(1000, 0, 0);
        CityState state = new CityState(0, 0, 0, 0, 0);

        assertEquals(
                0.0,
                calculator.calculate(snapshot, state),
                1e-9
        );
    }

    @Test
    void happinessCannotExceedOneHundred() {
        GridSnapshot snapshot = snapshot(0, 0, 0);
        CityState state = new CityState(0, 100, 0, 0, 0);

        assertEquals(
                100.0,
                calculator.calculate(snapshot, state),
                1e-9
        );
}
}
