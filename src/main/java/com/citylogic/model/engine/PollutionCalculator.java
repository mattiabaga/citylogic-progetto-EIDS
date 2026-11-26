package com.citylogic.model.engine;

import com.citylogic.model.core.GridSnapshot;


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
