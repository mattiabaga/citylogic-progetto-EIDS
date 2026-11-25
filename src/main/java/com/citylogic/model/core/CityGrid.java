package com.citylogic.model.core;

import java.util.Collections;
import java.util.List;

public class CityGrid {
    private int width;  
    private int height;
    private Cell[][] cells;

    public boolean isInside(int x, int y) {
        return false;
    }

    public boolean canBuild(Building b, int x, int y) {
        return false;
    }

    public void placeBuilding(Building b, int x, int y) {
        // TODO
    }

    public List<Cell> getNeighbors(int x, int y) {
        return Collections.emptyList();
    }

    public Cell getCell(int x, int y){
        if(x < 0 || x >= width)
            throw new IllegalArgumentException("Invalid coordinate for grid");

        if(y < 0 || y >= height)
            throw new IllegalArgumentException("Invalid ordinate for grid");

        return cells[x][y];
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
