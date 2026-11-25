package com.citylogic.model.core.buildings;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.City;

public class Park extends Building {
    private double pollutionReduction;

    public Park(String id, int cost, double maintenance, double pollutionProduced, double pollutionReduction){
        super(id, cost, maintenance, pollutionProduced);

        if(pollutionReduction < 0)
            throw new IllegalArgumentException("Park value cannot be negative");

        this.pollutionReduction=pollutionReduction;
    }

    public double getPollutionReduction(){
        return pollutionReduction;
    }
    @Override
    public void onTick(City city) {
        // TODO
    }
}
