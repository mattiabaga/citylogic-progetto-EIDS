
package com.citylogic.model.core;

import com.citylogic.model.observer.MetricType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    private City createCityWithBudget(double budget) {
        City city = new City();

        if (budget > 0) {
            city.applyTickResults(0, 0, 0, 0, budget);
        }

        return city;
    }

    @Test
    void spendSubtractsAmountFromBudget() {
        City city = createCityWithBudget(100);

        city.spend(30);

        assertEquals(70, city.getBudget(), 0.0001);
    }

    @Test
    void spendRejectsNegativeAmount() {
        City city = createCityWithBudget(100);

        assertThrows(
                IllegalArgumentException.class,
                () -> city.spend(-10)
        );

        assertEquals(100, city.getBudget(), 0.0001);
    }

    @Test
    void spendRejectsNonFiniteAmount() {
        City city = createCityWithBudget(100);

        assertThrows(
                IllegalArgumentException.class,
                () -> city.spend(Double.NaN)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> city.spend(Double.POSITIVE_INFINITY)
        );

        assertEquals(100, city.getBudget(), 0.0001);
    }

    @Test
    void spendRejectsAmountGreaterThanBudget() {
        City city = createCityWithBudget(50);

        assertThrows(
                IllegalStateException.class,
                () -> city.spend(60)
        );

        assertEquals(50, city.getBudget(), 0.0001);
    }

    @Test
    void applyTickResultsUpdatesStateAndBudget() {
        City city = new City();

        city.applyTickResults(10, 60, 5, 0, 100);

        assertEquals(1, city.getTick());
        assertEquals(100, city.getBudget(), 0.0001);

        CityState state = city.getCityState();

        assertEquals(10, state.getPopulation());
        assertEquals(60, state.getHappiness(), 0.0001);
        assertEquals(5, state.getPollution(), 0.0001);
        assertEquals(0, state.getEnergy(), 0.0001);
        assertEquals(100, state.getIncome(), 0.0001);
    }

    @Test
    void applyTickResultsSubtractsNegativeIncomeFromBudget() {
        City city = createCityWithBudget(100);

        city.applyTickResults(0, 0, 0, 0, -30);

        assertEquals(70, city.getBudget(), 0.0001);
        assertEquals(-30, city.getCityState().getIncome(), 0.0001);
        assertEquals(2, city.getTick());
    }

    @Test
    void insufficientBudgetDoesNotApplyTickResults() {
        City city = createCityWithBudget(20);

        CityState originalState = city.getCityState();
        int originalTick = city.getTick();

        AtomicInteger notifications = new AtomicInteger(0);
        city.addObserver(updatedCity ->
                notifications.incrementAndGet()
        );

        assertThrows(
                IllegalStateException.class,
                () -> city.applyTickResults(5, 50, 0, 0, -30)
        );

        assertEquals(20, city.getBudget(), 0.0001);
        assertSame(originalState, city.getCityState());
        assertEquals(originalTick, city.getTick());
        assertEquals(0, notifications.get());
    }

    @Test
    void invalidCityStateDoesNotModifyCity() {
        City city = createCityWithBudget(100);

        CityState originalState = city.getCityState();
        int originalTick = city.getTick();

        assertThrows(
                IllegalArgumentException.class,
                () -> city.applyTickResults(-1, 50, 0, 0, 50)
        );

        assertEquals(100, city.getBudget(), 0.0001);
        assertSame(originalState, city.getCityState());
        assertEquals(originalTick, city.getTick());
    }

    @Test
    void observersAreNotifiedAfterSuccessfulUpdate() {
        City city = new City();

        AtomicInteger observedTick = new AtomicInteger(-1);

        city.addObserver(updatedCity -> {
            assertEquals(1, updatedCity.getTick());
            assertEquals(50, updatedCity.getBudget(), 0.0001);
            assertEquals(
                    10,
                    updatedCity.getCityState().getPopulation()
            );

            observedTick.set(updatedCity.getTick());
        });

        city.applyTickResults(10, 50, 0, 0, 50);

        assertEquals(1, observedTick.get());
    }
}
