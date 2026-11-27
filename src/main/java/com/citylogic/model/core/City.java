package com.citylogic.model.core;

import java.util.List;
import java.util.ArrayList;
import com.citylogic.model.observer.Observer;

public class City {
    private String name;
    private double budget;
    private int tick;
    private PolicyManager policies;
    private CityGrid grid;
    private CityState state;
    private List<Observer> observerList;

    //getter
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

    //costruttori
    public City(){
        name = "DEFAULT";
        budget = 0;
        tick = 0;
        policies = null;
        grid = new CityGrid();
        state = new CityState();
        observerList = new ArrayList<Observer>();
    }
    
    public void spend(double amount) {
        // TODO
    }

    public void applyTickResults(   int population,
                                    double happiness,
                                    double pollution,
                                    double energy,//NOT IN USE
                                    double income)
    {

    }

    public void addObserver(Observer o) {
        if(o == null)
            throw new IllegalArgumentException("Observer cannot be null");

        if(!observerList.contains(o))
            observerList.add(o);
        
    }

    public void removeObserver(Observer o) {
        if(o == null)
            throw new IllegalArgumentException("Observer cannot be null");
        
        if(!observerList.remove(o))
            throw new IllegalArgumentException("Observer was not in observerList");
    }

    private void notifyObservers() {
        for (Observer observer : new ArrayList<>(observerList)) //la copia della lista serve a impedire problemi se durante la notifica viene chiamato add o remove
            observer.update(this);  //il parametro passato è l'oggetto City
    }
}
