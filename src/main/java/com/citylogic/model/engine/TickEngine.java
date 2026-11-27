package com.citylogic.model.engine;

import com.citylogic.model.core.City;
import com.citylogic.model.core.CityState;

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
        //analisi della griglia
        GridSnapshot snapshot = gridAnalyzer.analyze(city.getCityGrid());

        //analisi dei nuovi stati
        CityState state = city.getCityState();
        int population = populationCalculator.calculate(snapshot, state);
        double happiness = happinessCalculator.calculate(snapshot, state);
        double pollution = pollutionCalculator.calculate(snapshot);
        double energy = 0;  //NOT IN USE
        double income = economyCalculator.calculate(snapshot, state);

        //applicazione degli effetti
        city.applyTickResults(population, happiness, pollution, energy, income);

    }
}
