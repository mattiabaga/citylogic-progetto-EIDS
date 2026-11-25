package com.citylogic.model.core;

import com.citylogic.model.observer.Observer;

public class City {
    private String name;
    private double budget;
    private int tick;
    private PolicyManager policies;
    private CityGrid grid;
    private CityState state;

    public String getName(){
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public int getTick(){
        return tick;
    }

    public PolicyManager getPolicyManager(){
        return policies;
    }
    
    public CityGrid getCityGrid(){
        return grid;
    }

    public CityState getCityState(){
        return state;
    }
    
    public void spend(double amount) {
        // TODO
    }

    public void addObserver(Observer o) {
        // TODO
    }

    public void removeObserver(Observer o) {
        // TODO
    }
}
