package com.citylogic.model.engine;

import com.citylogic.model.core.CityState;

public class HappinessCalculator {
    public double calculate(GridSnapshot snapshot, CityState state){
        if(snapshot == null)
            throw new IllegalArgumentException("GridSnapshot cannot be null");
        if(state == null)
            throw new IllegalArgumentException("CityState cannot be null");

        /*CALCOLO DELLA PENALITà DA SOVRAPOPOLAMENTO */
        int population = state.getPopulation();
        int maxPopulation = snapshot.getActiveHouseCapacity();

        double occupationRate = 0;  //Se la popolazione è pari a zero non ci dovrebbe essere una penalità DA DISCUTERE!!
        if(maxPopulation != 0)
            occupationRate = (double) population / (double) maxPopulation;
        //      R = P(k) / mP

        double kPopMaxPenalty = 30;
        double kOccRatePenalty = 0.85;
        
        double populationPenalty = kPopMaxPenalty * (occupationRate - kOccRatePenalty) / (1 - kOccRatePenalty);
        //  Op = kp * (R - ko)/(1 - ko)

        if(populationPenalty < 0)
            populationPenalty = 0;
        
        /*CALCOLO DELLA PENALITà DA INQUINAMENTO */
        double pollution = snapshot.getTotalPollutionProduced() - snapshot.getTotalPollutionReduction();

        double kPolPenalty = 0.3;

        double pollutionPenalty = Math.max(0, pollution * kPolPenalty); 
        //  Ol = kl * L

        /*CALCOLO DELLA HAPPINESS ATTESA*/
        double expectedHappiness = Math.max(0, 100 - populationPenalty - pollutionPenalty); 
        //  He = 100 - Op - Ol appartentente a [100, 0]

        /*CALCOLO DELLA HAPPINES IN K+1*/
        double happiness = state.getHappiness();
        double kAdaptation = 0.2;

        happiness = happiness + kAdaptation * (expectedHappiness - happiness);
        // H(k+1) = H(k) + Ka * (He - H(k) )

        if(happiness < 0)
            return 0;
        if(happiness > 100)
            return 100;
        return happiness;
    }
    
}
