package com.citylogic.model.engine;

import com.citylogic.model.core.City;
import com.citylogic.model.core.CityGrid;
import com.citylogic.model.core.buildings.Park;
import com.citylogic.model.core.Cell;
import com.citylogic.model.core.Building;

public class PollutionCalculator {
    public double calculate(City city) {
        if(city == null)
            throw new IllegalArgumentException("City object had not be initialized");

        double pollProdSum = 0;     //Sum of all building pollutionProduction
        double pollReduSum = 0;     //Sum of all building pollutionReduction

        CityGrid grid = city.getCityGrid();
        if(grid == null)
            throw new IllegalArgumentException("CityGrid object had not be intialized");

        Cell cell;
        Building building;

        for(int x = 0; x < grid.getWidth(); x++ )
            for(int y = 0; y < grid.getHeight(); y++){

                cell = grid.getCell(x,y);
                if(cell == null)
                    throw new IllegalArgumentException("Cell object had not be intialized");

                if(cell.isOccupied()){  

                    building = cell.getBuilding();
                    if(building == null)    //dovrebbe essere inutile poichè attualmente isOccupied() controlla esattamente questo
                        throw new IllegalArgumentException("Building object had not be intialized");

                    pollProdSum += building.getPollutionProduced();

                    if(building instanceof Park){
                        Park park = (Park) building;
                        pollReduSum += park.getPollutionReduction();
                    }
                }
            }
        
        if(pollProdSum - pollReduSum < 0)   //per scelta l'inquinamento progettuale non può essere negativo
            return 0.0;
        return pollProdSum - pollReduSum;
    }
}
