
package com.citylogic.model;

import com.citylogic.model.core.City;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private City getCity(GameController controller)
            throws NoSuchFieldException, IllegalAccessException {

        Field cityField = GameController.class.getDeclaredField("city");
        cityField.setAccessible(true);

        return (City) cityField.get(controller);
    }

    @Test
    void constructorInitializesCityAndTickEngine() {
        GameController controller = new GameController();

        assertNotNull(controller);

        assertDoesNotThrow(controller::nextTick);
    }

    @Test
    void nextTickAdvancesCityTickByOne() throws Exception {
        GameController controller = new GameController();

        City city = getCity(controller);

        assertEquals(0, city.getTick());

        controller.nextTick();

        assertEquals(1, city.getTick());

        controller.nextTick();

        assertEquals(2, city.getTick());
    }
}
