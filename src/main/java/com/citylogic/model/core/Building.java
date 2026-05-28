package com.citylogic.model.core;

public abstract class Building {
    private String id;
    private int cost;
    private double maintenance;
    private double pollutionProduced;

    public void onTick(City city) {
        // TODO
    }

    public int getPopulationImpact() {
        return 0;
    }

    public double getEnergyConsumption() {
        return 0.0;
    }

    public double getPollutionProduced() {
        return 0.0;
    }
}
