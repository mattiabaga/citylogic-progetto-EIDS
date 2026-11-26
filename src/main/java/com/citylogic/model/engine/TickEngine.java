package com.citylogic.model.engine;

import com.citylogic.model.core.City;

public class TickEngine {
    private final City city;
    private final GridAnalyzer gridAnalyzer;
    private final PollutionCalculator pollutionCalculator;
    private final EconomyCalculator economyCalculator;
    private final PopulationCalculator populationCalculator;
    private final HappinessCalculator happinessCalculator;

    public TickEngine(City city){
        if(city == null)
            throw new IllegalArgumentException("City cannot be null");

        this.city = city;
        gridAnalyzer = new GridAnalyzer();
        pollutionCalculator = new PollutionCalculator();
        economyCalculator = new EconomyCalculator();
        populationCalculator = new PopulationCalculator();
        happinessCalculator = new HappinessCalculator();
    }

    public void tick() {
        // TODO
    }
}
