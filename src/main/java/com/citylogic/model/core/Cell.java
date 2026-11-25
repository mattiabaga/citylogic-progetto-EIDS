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

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Building getBuilding() {
        return building;
    }

    public TerrainType getTerrainType(){
        return terrain;
    }

    public boolean getAceessible(){
        return accessible;
    }

    public void setBuilding(Building b) {
        // TODO
    }
}
