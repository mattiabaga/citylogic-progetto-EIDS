package com.citylogic.model.core;

import com.citylogic.model.observer.MetricType;

public class CityState {
    private int population;
    private double happiness;
    private double pollution;
    private double energy;      //NOT IN USE
    private double income;

    public int gePopulation(){
        return population;
    }

    public double getHappiness(){
        return happiness;
    }

    public double getPollution(){
        return pollution;
    }

    public double getEnergy(){
        return energy;
    }

    public double getIncome(){
        return income;
    }

    public void update(MetricType metric, double value) {
        if(!Double.isFinite(value))
            throw new IllegalArgumentException(metric.name() + " value must be finite");

        switch(metric){

            case POPULATION:
                if(value != Math.floor(value) || value < 0 || value > Integer.MAX_VALUE)
                    throw new IllegalArgumentException("Population must be non negative integer");

                population = (int) value;
                break;

            case HAPPINESS:
                if(value < 0)
                    throw new IllegalArgumentException("Happiness cannot be negative");

                happiness = value;
                break;

            case POLLUTION:
                if(value < 0)
                    throw new IllegalArgumentException("Pollution cannot be negative");

                pollution = value;
                break;

            case ENERGY:
                if(value < 0)
                    throw new IllegalArgumentException("Energy cannot be negative");
                energy = value;
                break;

            case INCOME:
                income = value;     //THE INCOME CAN BE <= 0
                break;

            default:
                throw new IllegalArgumentException("Unsupported metric: " + metric.name());
        }
        
    }

    public void notifyObservers() {
        // TODO
    }
}
