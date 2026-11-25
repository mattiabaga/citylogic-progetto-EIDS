package com.citylogic.model.core;

public abstract class Building {
    private String id;
    private int cost;
    private double maintenance;
    private double pollutionProduced;

    protected Building(String id, int cost, double maintenance, double pollutionProduced){
        if(id==null || id.isBlank())
            throw new IllegalArgumentException("Building ID cannot be empty");

        if(cost < 0 || maintenance < 0 || pollutionProduced < 0) 
            throw new IllegalArgumentException("Building values cannot be negative");

        this.id=id;
        this.cost=cost;
        this.maintenance=maintenance;
        this.pollutionProduced=pollutionProduced;
    }  

    public String getId(){
        return id;
    }

    public int getCost(){
        return cost;
    }

    public double getMaintenance(){
        return maintenance;
    }

    public double getPollutionProduced(){
        return pollutionProduced;
    }

     //da sistemare
    public void onTick(City city) {
        // TODO
    }

    public int getPopulationImpact() {
        return 0;
    }

    public double getEnergyConsumption() {  //DOVREMMO FARLA COME BOOL 
        return 0.0;
    }

}
