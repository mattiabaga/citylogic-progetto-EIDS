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
}
