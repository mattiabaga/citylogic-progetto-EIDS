package com.citylogic.model.engine;

import com.citylogic.model.core.Building;
import com.citylogic.model.core.City;
import com.citylogic.model.core.CityGrid;
import com.citylogic.model.core.buildings.FactoryBuilding;
import com.citylogic.model.core.buildings.Shop;
import com.citylogic.model.core.Cell;

public class EconomyCalculator {
    public double calculate(City city) {
        if(city == null)
            throw new IllegalArgumentException("City object had not be initialized");

        double incomeSum = 0;       //sum of income - maintenance
        int workers = city.getCityState().gePopulation();       //otteniamo la popolazione totale 

        CityGrid grid = city.getCityGrid();
        if(grid == null)
            throw new IllegalArgumentException("CityGrid object had not be intialized");

        Cell cell;
        Building building;

        for(int x = 0; x < grid.getWidth(); x++)
            for(int y = 0; y < grid.getHeight(); y++){
        
                cell = grid.getCell(x, y);
                if(cell == null)
                    throw new IllegalArgumentException("Cell object had not be initialized");

                if(cell.isOccupied()){

                    building = cell.getBuilding();
                    if(building == null)        //dovrebbe essere inutile poichè attualmente isOccupied() controlla esattamente questo
                        throw new IllegalArgumentException("Building object had not be initialized");

                    incomeSum += building.getMaintenance();     //il costo di mantenimento è comune a tutti gli edifici

                    //sezione delle factory
                    if(building instanceof FactoryBuilding){

                        FactoryBuilding factory = (FactoryBuilding) building;

                        workers -= factory.getWorkersNeeded();

                        if(workers > 0)     //se ci sono abbastanza persone a lavorare allora la fabbrica produce
                            incomeSum += factory.getProduction();

                    }else if(building instanceof Shop){     //sezione degli shop

                        Shop shop = (Shop) building;

                        incomeSum += shop.getIncomeBonus() * city.getCityState().getHappiness() / 100;
                        // il guadagno dello shop è proporzionale alla happiness, la quale è compresa tra 0 e 100

                    }
                }
            }

        return incomeSum;
    }
}
