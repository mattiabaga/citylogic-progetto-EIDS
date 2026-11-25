package com.citylogic.model.core;

public class Cell {
    private int x;
    private int y;
    private Building building;
    private TerrainType terrain;
    private boolean accessible;     //cosa intendiamo con accessibile?? se è collegata ad una strada? 

    public boolean isOccupied() {
        return building != null;
    }

    public void setBuilding(Building b) {
        // TODO
    }

    public Building getBuilding() {
        return building;
    }
}
