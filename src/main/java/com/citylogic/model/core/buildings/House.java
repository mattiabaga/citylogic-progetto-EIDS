package com.citylogic.model.core.buildings;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.City;

public class House extends Building {
    private int capacity;
    private boolean hasEnergy;

    public House(String id, int cost, double maintenance, double pollutionProduced, int capacity){
        super(id, cost, maintenance, pollutionProduced);

        if(capacity < 0)
            throw new IllegalArgumentException("House capacity cannot be negative");

        this.capacity=capacity;
        this.hasEnergy=false;
    }

    public boolean hasEnergy(){
        return hasEnergy;
    }

    public void setHasEnergy(boolean hasEnergy){
        this.hasEnergy=hasEnergy;
    }
    
    public int getCapacity(){
        return capacity;
    }

    //da fare
    @Override
    public void onTick(City city) {
        // TODO
    }
}
