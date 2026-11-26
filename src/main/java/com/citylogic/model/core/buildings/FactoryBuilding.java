package com.citylogic.model.core.buildings;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.City;

public class FactoryBuilding extends Building {
    private int workersNeeded;
    private double production;

    public FactoryBuilding(String id, int cost, double maintenance, double pollutionProduced, int workerNeeded, double production){
        super(id, cost, maintenance, pollutionProduced);

        if(workerNeeded < 0 || production < 0){
            throw new IllegalArgumentException("FactoryBuilding values cannot be negative");
        }

        this.workersNeeded=workerNeeded;
        this.production=production;
    }

    public int getWorkerNeeded(){
        return workersNeeded;
    }

    public double getProduction(){
        return production;
    }
    @Override
    public void onTick(City city) {
        // TODO
    }

}


