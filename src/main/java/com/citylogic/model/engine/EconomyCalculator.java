package com.citylogic.model.engine;

import com.citylogic.model.core.CityState;

/*
    Il calcolatore dell'economia calcola il guadagno al tempo k+1 prendendo i valori al tempo k

    -FactoryBuilding
    il guadagno delle fabbriche è pari alla sommatoria della produzione di tutte le factory se la popolazione è maggiore o uguale ai lavoratori necessari a far funzionare tutte le factory
    altrimenti la produzione è moltiplicata per il rapporto tra la popolazione totale e i lavoratori necessari
    in questo modo se si hanno 50 abitanti e 100 lavoratori necessari per le fabbriche, il guadagno sarà (guadagno totale delle fabbriche)* 50/100

    -Shop
    il guadagno degli shop è pari alla sommatoria del guadagno di tutti gli shop per la felicità in percentuale

    -Maintenace
    la spesa per il mantenimento di tutti gli edifici

*/
public class EconomyCalculator {
    public double calculate(GridSnapshot snapshot, CityState state) {
        if(snapshot == null)
            throw new IllegalArgumentException("GridSnapshot cannot be null");

        if(state == null)
            throw new IllegalArgumentException("CityState cannot be null");

        double totalIncome = 0;

        //calcolo del guadagno per le factory
        int workerNeeded = snapshot.getTotalWorkerNeeded();
        if(workerNeeded < 1)
            workerNeeded = 1;

        totalIncome += snapshot.getTotalFactoryProduction() * Math.min(1,(double) state.getPopulation() / workerNeeded);
            /*
            La formula sarebbe:
                                    I(t+1) = fp * min(1, p(t) / wn)

            dove I è il guadagno, fp è la produzione delle factory, p è la popolazione, wn sono i lavoratori necessari, t è il tempo
            */

        //calcolo per il guadagno degli shop
        totalIncome += snapshot.getTotalShopIncomeBonus() * state.getHappiness() / 100;
            /*
            La formula sarebbe:
                                    I(t+1) = si * H(t)

            dove I è il guadagno, si è il guadagno dato dagli shop, H è la happiness in percentuale, t è il tempo
            */

        //calcolo del costo di manutenzione di tutti gli edifici
        totalIncome -= snapshot.getTotalMaintenance();

        return totalIncome;
    }
}
