package com.citylogic.model.engine;

import com.citylogic.model.core.CityState;

/*
Il calcolatore calcola la popolazione al tempo k+1 partendo dai valori a tempo k
seguendo le seguenti formule:
            P(k+1) = min[P(k) + deltaP, maxP]

            deltaP = (maxP-P(k)) * a * (H(k) - minH)/100

    dove:
    P(k) è la popolazione al tempo k 
    maxP è la popolazione che tutte le case attive possono contenere
    deltaP è la crescita di popolazione (CHE PUò ESSERE ANCHE NEGATIVA!!)
    a è un coefficiente di crescita arbitrario
    H(k) è la felicità al tempo k
    minH è il valore di felicità che separa la crescita dal decadimento della popolazione

*/

public class PopulationCalculator {
    public int calculate(GridSnapshot snapshot, CityState state){

        int population = state.getPopulation();
        double deltaPopulation = 0;
        int maxPopulation = snapshot.getActiveHouseCapacity();
        double alpha = 0.5;
        int minHappiness = 30;

        if(population > maxPopulation)
            throw new IllegalStateException("Population is greater than max capacity");     //BISOGNA DECIDERE L'ECCEZIONE IN MODO CHE IL GAMECONTROLLER POSSA GESTIRE QUESTA SITUAZIONE
                                                                                            //NON è COMPITO DI POPULATIONCALCULATOR DI GESTIRE QUESTA SITUAZIONE
        deltaPopulation = (double) (maxPopulation - population) * alpha * (double) (state.getHappiness() - minHappiness) / 100;
        /*
        la formula sarebbe:
                            deltaP = (maxP-P(k)) * a * (H(k) - minH)/100
        */

        population = Math.min(population + (int) Math.ceil(deltaPopulation), maxPopulation);
        /*
        la formula sarebbe:
                            P(k+1) = min[P(k) + deltaP, maxP]
        */

        return Math.max(0, population);     //serve a coprire il caso in cui la popolazione sia diventata negativa
    }
}
