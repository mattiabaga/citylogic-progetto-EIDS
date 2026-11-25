package com.citylogic.model.core.buildings;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.City;

public class PowerPlant extends Building {
    private double energyProduced;  //NOT IN USE
    private int energyRange;        //IS INTENDED TO BE THE DISTANCE OF X AND Y IN THE GRID, SO A RECTANGLE

    public PowerPlant(String id, int cost, double maintenance, double pollutionProduced, double energyProduced, int energyRange){
        super(id, cost, maintenance, pollutionProduced);

        if(energyProduced < 0)
            throw new IllegalArgumentException("PowerPlant energyProduced cannot be negative");

        this.energyProduced=energyProduced;

        if(energyRange <=0)
            throw new IllegalArgumentException("PowerPlant energyRange cannot be negative or equal to zero");
        this.energyRange=energyRange;
    }

    public double getEnergyProduced(){   //NOT IN USE
        return energyProduced;
    }

    public int getEnergyRadius(){
        return energyRange;
    }

    @Override
    public void onTick(City city) {
        // TODO
    }
}
