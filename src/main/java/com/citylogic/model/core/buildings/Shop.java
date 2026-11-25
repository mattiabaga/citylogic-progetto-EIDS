package com.citylogic.model.core.buildings;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.City;

public class Shop extends Building {
    private double incomeBonus;

    public Shop(String id, int cost, double maintenance, double pollutionProduced, double incomeBonus){
        super(id, cost, maintenance, pollutionProduced);

        if(incomeBonus < 0)
            throw new IllegalArgumentException("Shop value cannot be negative");

        this.incomeBonus=incomeBonus;
    }

    public double getIncomeBonus(){
        return incomeBonus;
    }
    @Override
    public void onTick(City city) {
        // TODO
    }
}
