package com.citylogic.model.core;

import com.citylogic.model.observer.MetricType;

public class CityState {
    private int population;
    private double happiness;
    private double pollution;
    private double energy;      //NOT IN USE
    private double income;

    //getter
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

    //costruttori
    public CityState(){
        population = 0;
        happiness = 0;
        pollution = 0;
        energy = 0;
        income = 0;
    }

    public CityState(int population, double happiness, double pollution, double energy, double income){
        this();

        this.update(MetricType.POPULATION, population);
        this.update(MetricType.HAPPINESS, happiness);
        this.update(MetricType.POLLUTION, pollution);
        this.update(MetricType.ENERGY, energy);
        this.update(MetricType.INCOME, income);

    }

    public CityState(CityState state){
        if(state == null)
            throw new IllegalArgumentException("CityState cannot be null");  
        population = state.population;
        happiness = state.happiness;
        pollution = state.pollution;
        energy = state.energy;
        income = state.income;
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

}
