
package com.citylogic.model.engine;

import com.citylogic.model.core.City;
import com.citylogic.model.core.CityState;
import com.citylogic.model.observer.MetricType;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TickEngineTest {

    @Test
    void tickAdvancesAndUpdatesCityState() {
        City city = new City();
        city.getCityState().update(MetricType.HAPPINESS, 50);

        TickEngine engine = new TickEngine(city);

        engine.tick();

        assertEquals(1, city.getTick());

        CityState state = city.getCityState();

        assertEquals(0, state.getPopulation());
        assertEquals(60.0, state.getHappiness(), 0.0001);
        assertEquals(0.0, state.getPollution(), 0.0001);
        assertEquals(0.0, state.getEnergy(), 0.0001);
        assertEquals(0.0, state.getIncome(), 0.0001);
        assertEquals(0.0, city.getBudget(), 0.0001);
    }

    @Test
    void observersAreNotifiedAfterSuccessfulTick() {
        City city = new City();
        city.getCityState().update(MetricType.HAPPINESS, 50);

        AtomicInteger observedTick = new AtomicInteger(-1);
        AtomicReference<Double> observedHappiness =
                new AtomicReference<>();

        city.addObserver(updatedCity -> {
            observedTick.set(updatedCity.getTick());
            observedHappiness.set(
                    updatedCity.getCityState().getHappiness()
            );
        });

        TickEngine engine = new TickEngine(city);

        engine.tick();

        assertEquals(1, observedTick.get());
        assertEquals(60.0, observedHappiness.get(), 0.0001);
    }

    @Test
    void invalidPopulationDoesNotApplyTickResults() {
        City city = new City();

        // La griglia vuota ha capacità abitativa pari a zero.
        city.getCityState().update(MetricType.POPULATION, 1);

        CityState originalState = city.getCityState();

        AtomicInteger notifications = new AtomicInteger(0);
        city.addObserver(updatedCity ->
                notifications.incrementAndGet()
        );

        TickEngine engine = new TickEngine(city);

        assertThrows(
                IllegalStateException.class,
                engine::tick
        );

        assertEquals(0, city.getTick());
        assertSame(originalState, city.getCityState());
        assertEquals(1, city.getCityState().getPopulation());
        assertEquals(0, notifications.get());
    }

    @Test
    void constructorRejectsNullCity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TickEngine(null)
        );
    }

    @Test
    void repeatedTicksAreDeterministicForIdenticalInitialStates() {
        City city1 = new City();
        City city2 = new City();

        city1.getCityState().update(MetricType.HAPPINESS, 50);
        city2.getCityState().update(MetricType.HAPPINESS, 50);

        TickEngine engine1 = new TickEngine(city1);
        TickEngine engine2 = new TickEngine(city2);

        double[] expectedHappiness = {60.0, 68.0, 74.4};

        for (int i = 0; i < 3; i++) {
            engine1.tick();
            engine2.tick();

            assertEquals(i + 1, city1.getTick());
            assertEquals(i + 1, city2.getTick());

            assertEquals(
                    expectedHappiness[i],
                    city1.getCityState().getHappiness(),
                    1e-9
            );

            assertEquals(
                    city1.getCityState().getHappiness(),
                    city2.getCityState().getHappiness(),
                    1e-9
            );

            assertEquals(
                    city1.getCityState().getPopulation(),
                    city2.getCityState().getPopulation()
            );

            assertEquals(
                    city1.getBudget(),
                    city2.getBudget(),
                    1e-9
            );
        }
    }
}
