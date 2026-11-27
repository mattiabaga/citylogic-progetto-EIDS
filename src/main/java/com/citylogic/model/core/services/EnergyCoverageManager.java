package com.citylogic.model.core.services;

import java.util.ArrayList;
import java.util.List;

import com.citylogic.model.core.CityGrid;
import com.citylogic.model.core.Cell;
import com.citylogic.model.core.Building;
import com.citylogic.model.core.buildings.House;
import com.citylogic.model.core.buildings.PowerPlant;

public class EnergyCoverageManager {

    public void updateCoverage(CityGrid grid) {

        if (grid == null)
            throw new IllegalArgumentException("CityGrid cannot be null");

        List<Cell> houses = new ArrayList<>();
        List<Cell> powerPlants = new ArrayList<>();

        // Individuazione di case e centrali
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {

                Cell cell = grid.getCell(x, y);

                if (!cell.isOccupied())
                    continue;

                Building building = cell.getBuilding();

                if (building instanceof House)
                    houses.add(cell);

                else if (building instanceof PowerPlant)
                    powerPlants.add(cell);
            }
        }

        // Calcolo della copertura energetica
        for (Cell houseCell : houses) {

            House house = (House) houseCell.getBuilding();
            boolean hasEnergy = false;

            for (Cell plantCell : powerPlants) {

                PowerPlant plant =
                        (PowerPlant) plantCell.getBuilding();

                int radius = plant.getEnergyRadius();

                long distanceX = Math.abs(
                        (long) houseCell.getX() - plantCell.getX());

                long distanceY = Math.abs(
                        (long) houseCell.getY() - plantCell.getY());

                if (distanceX <= radius && distanceY <= radius) {
                    hasEnergy = true;
                    break;
                }
            }

            house.setHasEnergy(hasEnergy);
        }
    }
}
