package com.citylogic.model.engine;

import java.util.ArrayList;
import java.util.List;
import com.citylogic.model.core.CityGrid;
import com.citylogic.model.core.Cell;
import com.citylogic.model.core.Building;
import com.citylogic.model.core.buildings.*;

    /*  IMPORTANTE:
    nel caso in cui venisssero aggiunte nuove sottoclassi di Building vanno implementati gli else if del metodo analyze!
     */
public class GridAnalyzer {
    
    public GridSnapshot analyze(CityGrid grid){
        
        if(grid == null)
            throw new IllegalArgumentException("CityGrid cannot be null");

        double totalPollutionProduced = 0;
        double totalPollutionReduction = 0;
        int totalHouseCapacity = 0;
        int activeHouseCapacity = 0;
        double totalMaintenance = 0;
        int totalWorkNeeded = 0;
        double totalFactoryProduction = 0;
        double totalEnergyProduced = 0;
        double totalShopIncomeBonus = 0;

        for(int x = 0; x < grid.getWidth(); x++)
            for(int y = 0; y < grid.getHeight(); y++){
                
                Cell cell = grid.getCell(x, y);

                if(cell.isOccupied()){

                    Building building = cell.getBuilding();

                    //variabili della superclasse
                    totalPollutionProduced += building.getPollutionProduced();
                    totalMaintenance += building.getMaintenance();

                    //variabili delle sottoclassi
                    if(building instanceof House){
                        House house = (House) building;

                        totalHouseCapacity += house.getCapacity();

                        if(house.hasEnergy())
                            activeHouseCapacity += house.getCapacity();

                    }else if(building instanceof FactoryBuilding){
                        FactoryBuilding factory = (FactoryBuilding) building;

                        totalWorkNeeded += factory.getWorkerNeeded();
                        totalFactoryProduction += factory.getProduction(); 

                    }else if(building instanceof Shop){
                        Shop shop = (Shop) building;

                        totalShopIncomeBonus += shop.getIncomeBonus();

                    }else if(building instanceof Park){
                        Park park = (Park) building;

                        totalPollutionReduction += park.getPollutionReduction();

                    }else if(building instanceof PowerPlant){
                        PowerPlant powerPlant = (PowerPlant) building;

                        totalEnergyProduced += powerPlant.getEnergyProduced();
                    }else{  //nel caso in cui ci sia un Building non implementato in questa funzione
                        throw new IllegalStateException("Unsupported building type: " + building.getClass().getName());
                    }
                }
            }
         return new GridSnapshot(
                                totalPollutionProduced,
                                totalPollutionReduction,
                                totalHouseCapacity,
                                activeHouseCapacity,
                                totalMaintenance,
                                totalWorkNeeded,
                                totalFactoryProduction,
                                totalEnergyProduced,
                                totalShopIncomeBonus);
    }
}
