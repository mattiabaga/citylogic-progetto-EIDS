package com.citylogic.model.core;

public class Cell {
    private int x;
    private int y;
    private Building building;
    private TerrainType terrain;
    private boolean accessible;

    public boolean isOccupied() {
        return false;
    }

    public void setBuilding(Building b) {
        // TODO
    }

    public Building getBuilding() {
        return building;
    }
}
