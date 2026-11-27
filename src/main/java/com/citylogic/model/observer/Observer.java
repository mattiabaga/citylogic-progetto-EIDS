package com.citylogic.model.observer;

import com.citylogic.model.core.City;
import com.citylogic.model.core.CityState;

public interface Observer {
    void update(City city);
}
