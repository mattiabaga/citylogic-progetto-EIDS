
package com.citylogic.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusDashboardFormatterTest {

    @Test
    void formatsBudgetWithItalianSeparators() {
        String result = StatusDashboardFormatter.budget(12500);

        assertEquals("Budget: 12.500,00 crediti", result);
    }

    @Test
    void formatsNegativeBudget() {
        String result = StatusDashboardFormatter.budget(-500.25);

        assertEquals("Budget: -500,25 crediti", result);
    }

    @Test
    void formatsPopulationAndTick() {
        assertEquals("Popolazione: 1.250",
                StatusDashboardFormatter.population(1250));

        assertEquals("Tick: 1.000",
                StatusDashboardFormatter.tick(1000));
    }

    @Test
    void formatsHappinessAndPollution() {
        assertEquals("Felicità: 74,5%",
                StatusDashboardFormatter.happiness(74.5));

        assertEquals("Inquinamento: 32,0",
                StatusDashboardFormatter.pollution(32));
    }
}
