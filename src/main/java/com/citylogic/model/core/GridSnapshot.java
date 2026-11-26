package com.citylogic.model.core;

import java.util.List;

import com.citylogic.model.core.Building;

public class GridSnapshot {
    private final List<Building> buildings;

    private final double totalPollutionProduced;
    private final double totalPollutionReduction;
    private final int totalHouseCapacity;
    private final int activeHouseCapacity;
    private final double totalMaintenance;
    private final int totalWorkNeeded;
    private final double totalFactoryProduction;
    private final double totalEnergyProduced;
    private final double totalShopIncomeBonus;

    public GridSnapshot(List<Building> buildings,
                        double totalPollutionProduced,
                        double totalPollutionReduction,
                        int totalHouseCapacity,
                        int activeHouseCapacity,
                        double totalMaintenance,
                        int totalWorkNeeded,
                        double totalFactoryProduction,
                        double totalEnergyProduced,
                        double totalShopIncomeBonus)
    {
        
        if(buildings == null)
            throw new IllegalArgumentException("list of buildings not valid");
        this.buildings = List.copyOf(buildings);

        if(totalPollutionProduced < 0 || Double.isFinite(totalPollutionProduced))
            throw new IllegalArgumentException("TotalPollutionProduced not valid");
        this.totalPollutionProduced = totalPollutionProduced;

        if(totalPollutionReduction < 0 || Double.isFinite(totalPollutionReduction))
            throw new IllegalArgumentException("TotalPollutionReduction not valid");
        this.totalPollutionReduction = totalPollutionReduction;

        if(totalHouseCapacity < 0)
            throw new IllegalArgumentException("TotalHuoseCapacity not valid");
        this.totalHouseCapacity = totalHouseCapacity;

        if(activeHouseCapacity < 0 || activeHouseCapacity > totalHouseCapacity)
            throw new IllegalArgumentException("ActiveHuoseCapacity not valid");
        this.activeHouseCapacity = activeHouseCapacity;

        if(totalMaintenance < 0 || Double.isFinite(totalMaintenance))
            throw new IllegalArgumentException("TotalMaintenance not valid");
        this.totalMaintenance = totalMaintenance;

        if(totalWorkNeeded < 0)
            throw new IllegalArgumentException("TotalWorkNeeded not valid");
        this.totalWorkNeeded = totalWorkNeeded;

        if(totalFactoryProduction < 0 || Double.isFinite(totalFactoryProduction))
            throw new IllegalArgumentException("TotalFactoryIncome not valid");
        this.totalFactoryProduction = totalFactoryProduction;

        if(totalEnergyProduced < 0 || Double.isFinite(totalEnergyProduced))
            throw new IllegalArgumentException("TotalEnergyProduced not valid");
        this.totalEnergyProduced = totalEnergyProduced;

        if(totalShopIncomeBonus < 0 || Double.isFinite(totalShopIncomeBonus))
            throw new IllegalArgumentException("TotalShopIncomeBonus not valid");
        this.totalShopIncomeBonus = totalShopIncomeBonus;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public double getTotalPollutionProduced() {
        return totalPollutionProduced;
    }

    public double getTotalPollutionReduction() {
        return totalPollutionReduction;
    }

    public int getTotalHouseCapacity() {
        return totalHouseCapacity;
    }

    public int getActiveHouseCapacity() {
        return activeHouseCapacity;
    }

    public double getTotalMaintenance() {
        return totalMaintenance;
    }

    public int getTotalWorkNeeded() {
        return totalWorkNeeded;
    }

    public double getTotalFactoryProduction() {
        return totalFactoryProduction;
    }

    public double getTotalEnergyProduced() {
        return totalEnergyProduced;
    }

    public double getTotalShopIncomeBonus() {
        return totalShopIncomeBonus;
    }

}
