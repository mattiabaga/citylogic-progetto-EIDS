package com.citylogic.model.engine;

import com.citylogic.model.core.GridSnapshot;

/*
Il calcolatore definisce il livello di inquinamento al tempo k+1 partendo dallo stato attuale della griglia, considerabile come il tempo k

*/
public class PollutionCalculator {
    public double calculate(GridSnapshot gridSnapshot) {

        if(gridSnapshot == null)
            throw new IllegalArgumentException("GridSnapshot cannot be null");
        
        double pollution = gridSnapshot.getTotalPollutionProduced() - gridSnapshot.getTotalPollutionReduction();

        if(pollution < 0)
            return 0;
        
        return pollution;
    }
}
