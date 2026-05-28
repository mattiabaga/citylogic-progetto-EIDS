package com.citylogic.model.core;

import com.citylogic.model.observer.MetricType;

public class CityState {
    private int population;
    private double happiness;
    private double pollution;
    private double energy;
    private double income;

    public void update(MetricType metric, double value) {
        // TODO
    }

    public void notifyObservers() {
        // TODO
    }
}
