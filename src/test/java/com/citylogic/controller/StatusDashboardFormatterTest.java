
package com.citylogic.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusDashboardFormatterTest {

    @Test
    void formatsBudgetWithItalianSeparators() {
        String result = StatusDashboardFormatter.budget(12500);

        assertEquals("12.500,00 crediti", result);
    }

    @Test
    void formatsNegativeBudget() {
        String result = StatusDashboardFormatter.budget(-500.25);

        assertEquals("-500,25 crediti", result);
    }

    @Test
    void formatsPopulationAndTick() {
        assertEquals("1.250",
                StatusDashboardFormatter.population(1250));

        assertEquals("1.000",
                StatusDashboardFormatter.tick(1000));
    }

    @Test
    void formatsHappinessAndPollution() {
        assertEquals("74,5%",
                StatusDashboardFormatter.happiness(74.5));

        assertEquals("32,0",
                StatusDashboardFormatter.pollution(32));
    }

    @Test
    void formatsZeroValuesAtGameStart() {
        assertEquals("0,00 crediti", StatusDashboardFormatter.budget(0));
        assertEquals("0", StatusDashboardFormatter.population(0));
        assertEquals("0", StatusDashboardFormatter.tick(0));
    }
}
