package com.citylogic.model.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PollutionCalculatorTest {

    private final PollutionCalculator calculator = new PollutionCalculator();

    private GridSnapshot snapshot(double produced, double reduction) {
        return new GridSnapshot(
                produced, reduction,
                0, 0, 0, 0, 0, 0, 0
        );
    }

    @Test
    void returnsNetPollution() {
        assertEquals(7.0, calculator.calculate(snapshot(12, 5)), 1e-9);
    }

    @Test
    void clampsPollutionToZeroWhenReductionExceedsProduction() {
        assertEquals(0.0, calculator.calculate(snapshot(5, 12)), 1e-9);
    }

    @Test
    void returnsZeroWhenNoPollutionIsProducedOrReduced() {
        assertEquals(0.0, calculator.calculate(snapshot(0, 0)), 1e-9);
    }

    @Test
    void rejectsNullSnapshot() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(null));
    }

    @Test
    void sameSnapshotProducesConstantPollutionAcrossThreeCalculations() {
        GridSnapshot snapshot = snapshot(30, 0);

        for (int i = 0; i < 3; i++) {
            assertEquals(
                    30.0,
                    calculator.calculate(snapshot),
                    1e-9
            );
        }
    }
}
