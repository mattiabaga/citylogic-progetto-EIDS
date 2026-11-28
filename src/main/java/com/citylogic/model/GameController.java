package com.citylogic.model;

import com.citylogic.model.core.City;
import com.citylogic.model.core.PolicyStrategy;
import com.citylogic.model.engine.TickEngine;
import com.citylogic.model.infrastructure.BuildingFactory;
import com.citylogic.model.infrastructure.BuildingType;

public class GameController {
    private City city;
    private TickEngine tickEngine;
    private BuildingFactory buildingFactory;

    public GameController(){
        this.city = new City();
        this.tickEngine = new TickEngine(city);
        this.buildingFactory = new BuildingFactory();
    }

    public City getCity() {
        return city;
    }

    public void buildBuilding(BuildingType type, int x, int y) {
        // TODO
    }

    public void setPolicy(PolicyStrategy p) {
        // TODO
    }

    public void nextTick() {
        tickEngine.tick();
        // TODO
    }

    public void save(String path) {
        // TODO
    }

    public void load(String path) {
        // TODO
    }
}
