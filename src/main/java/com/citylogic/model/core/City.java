package com.citylogic.model.core;

import java.util.List;
import java.util.ArrayList;

import com.citylogic.model.core.services.EnergyCoverageManager;
import com.citylogic.model.observer.Observer;

public class City {
    private String name;
    private double budget;
    private int tick;
    private PolicyManager policies;
    private CityGrid grid;
    private CityState state;
    private List<Observer> observerList;
    private EnergyCoverageManager manager;

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
        manager = new EnergyCoverageManager();
    }
    
    //metodi specifici di City
    public void spend(double amount) {
         if (!Double.isFinite(amount))
            throw new IllegalArgumentException("Amount must be finite");

        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be negative");

        if (amount > budget)
            throw new IllegalStateException("Insufficient budget");

        budget -= amount;
    }

    public void applyTickResults(   int population,
                                    double happiness,
                                    double pollution,
                                    double energy,//NOT IN USE
                                    double income)
    {   
        //aggiornamento della copia di CityState
        CityState stateUpdate = new CityState(population, happiness, pollution, energy, income);

        //aggiornamento del budget
        if(income < 0)
            this.spend(-income);    //in questo modo la gestione di budget negativo è relegata a spend()
        else{
            double newBudget = this.budget + income;

            if(!Double.isFinite(newBudget))
                throw new ArithmeticException("Budget overflow");

            this.budget = newBudget;
        }

        //aggiornamento del CityState
        this.state = stateUpdate;     //in questo modo se CityState() o spend() non sono valide City non viene modificato 

        //aggiornamento dei tick
        this.tick++;

        //aggiornamento degli observer
        this.notifyObservers();     //DA AGGIUSTARE SE GLI OBSERVER POSSONO LANCIARE ECCEZIONI
    }

    //metodi per gli observer
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

    public void placeBuilding(Building building, int x, int y) {
        if (building == null)
            throw new IllegalArgumentException("Building cannot be null");

        grid.placeBuilding(building, x, y);

        manager.updateCoverage(grid);

        notifyObservers();
    }

    public Building removeBuilding(int x, int y) {
        Cell cell = grid.getCell(x, y);
        Building building = cell.getBuilding();

        if (building == null)
            throw new IllegalStateException("No building at the specified position");

        grid.removeBuilding(building, x, y);

        manager.updateCoverage(grid);

        notifyObservers();

        return building;
    }
}
