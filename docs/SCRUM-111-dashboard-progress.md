# SCRUM-111 — Dashboard e monitoraggio delle metriche globali

**Progetto:** Citylogic — Elementi di Ingegneria del Software  
**Responsabile di questa attività:** Antonio Del Piano  
**Branch di lavoro:** `feature/SCRUM-111-dashboard`  
**Stato:** Implementazione autonoma e prove iniziali completate; integrazione con il dominio ancora da fare.

## Obiettivo

Realizzare un pannello JavaFX che mostri **Budget, Popolazione, Felicità, Inquinamento e Tick**, mantenendo separata l'interfaccia grafica dalla logica della simulazione. Il pannello dovrà aggiornarsi quando cambiano le metriche: dopo un tick e dopo azioni immediate che incidono sulle risorse.

## Lavoro realizzato

1. Ho creato `status-dashboard.fxml`, un componente indipendente con cinque etichette e un'intestazione. Non ho modificato `hello-view.fxml`, per non sovrappormi al lavoro sulla grafica principale.
2. Ho creato `StatusDashboardController.java`: le etichette FXML sono collegate attraverso `@FXML` e il metodo `updateMetrics(...)` aggiorna i valori visualizzati.
3. Ho creato `StatusDashboardFormatter.java` per formattare le metriche secondo `Locale.ITALY`: separatori delle migliaia, due decimali per il budget, una cifra decimale per felicità e inquinamento, simbolo `%` per la felicità. Quando il budget è negativo, il controller ne cambia il colore in rosso.
4. Ho creato `DashboardPreview.java`, una piccola applicazione di prova separata dall'avvio principale. Visualizza la dashboard con dati fittizi e include due pulsanti: **Avanza tick** e **Costruisci strada (-500)**. Il primo aggiorna alcune metriche di esempio e il contatore; il secondo aggiorna subito il budget. Questa classe **non implementa le regole del gioco**.
5. Per consentire l'avvio della classe di prova nel progetto modulare, ho aggiunto a `module-info.java` l'istruzione `exports com.citylogic to javafx.graphics;` (da rivalutare quando la classe di anteprima non servirà più).
6. Ho aggiornato `pom.xml` aggiungendo `maven-surefire-plugin` versione `3.2.5` per l'esecuzione dei test JUnit 5.

## File interessati

| File | Tipo di intervento | Scopo |
|---|---|---|
| `src/main/resources/com/citylogic/view/status-dashboard.fxml` | Creato | Struttura visiva del pannello |
| `src/main/java/com/citylogic/controller/StatusDashboardController.java` | Creato | Aggiornamento delle etichette JavaFX |
| `src/main/java/com/citylogic/controller/StatusDashboardFormatter.java` | Creato | Formattazione indipendente dalla UI |
| `src/main/java/com/citylogic/DashboardPreview.java` | Creato, temporaneo | Prova manuale con valori simulati |
| `src/test/java/com/citylogic/controller/StatusDashboardFormatterTest.java` | Creato | Test unitari sulla formattazione |
| `src/main/java/module-info.java` | Modificato | Accesso a `DashboardPreview` da JavaFX |
| `pom.xml` | Modificato | Configurazione di Maven Surefire |

## Verifiche effettuate

**Test automatici:** eseguito `./mvnw.cmd clean test` con JDK 21 nel terminale Windows/PowerShell. Risultato osservato:

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

I quattro test JUnit riguardano il formato del budget positivo e negativo, la visualizzazione di popolazione/tick e quella di felicità/inquinamento. **Non sono ancora test automatici del flusso grafico o della simulazione.**

**Verifiche manuali:** ho avviato `DashboardPreview` e verificato che le cinque metriche si vedano e che entrambi i pulsanti aggiornino i valori visualizzati. Il controllo del colore rosso con budget negativo e l'integrazione con eventi reali restano da validare separatamente.

## Stato dei criteri di accettazione

| Criterio | Stato | Nota |
|---|---|---|
| AC1 — Aggiornamento dopo un tick reale | **Da integrare** | Il pannello si aggiorna nella preview, ma non riceve ancora eventi dal `TickEngine`. |
| AC2 — Aggiornamento dopo azioni immediate reali | **Da integrare** | Verificato solo il pulsante di prova per la spesa. |
| AC3 — Dashboard di sola lettura | **Parzialmente soddisfatto** | Il controller grafico visualizza dati ricevuti dall'esterno; manca la verifica dell'integrazione definitiva. |
| AC4 — Formattazione e feedback visivo | **Implementato; verifica parziale** | Formattazione coperta da 4 test JUnit; colore rosso previsto nel codice, da verificare manualmente. |

## Collegamento con il codice del gruppo: attività ancora necessarie

- `City.java` contiene `budget`, `tick` e un riferimento a `CityState`, ma nell'ultima versione esaminata espone soltanto `getBudget()`. Servirà concordare l'accesso in sola lettura al tick e allo stato.
- `CityState.java` contiene popolazione, felicità e inquinamento, ma nella versione esaminata non espone ancora i metodi pubblici necessari alla dashboard.
- `Observer`, `Dashboard` e i metodi di registrazione degli observer sono già previsti nella struttura del progetto, ma alcune implementazioni risultano ancora `TODO`.
- Occorre concordare con chi sviluppa il modello **quando notificare gli aggiornamenti** (dopo tick e dopo azioni immediate) e come passarli alla UI sul JavaFX Application Thread.
- Quando l'integrazione sarà pronta, aggiungere test di integrazione e verifiche di sistema basate sui veri acceptance criteria, non soltanto sulla preview.
- Decidere con il gruppo se mantenere `DashboardPreview` come strumento di sviluppo oppure rimuoverlo prima della consegna finale.

## Come riprodurre il lavoro

1. Aprire il progetto Maven in IntelliJ IDEA con JDK 21.
2. Eseguire i test dal terminale PowerShell del repository:

   ```powershell
   .\mvnw.cmd clean test
   ```

3. Avviare la classe `com.citylogic.DashboardPreview` con la configurazione JavaFX di IntelliJ.
4. Premere **Avanza tick** e **Costruisci strada (-500)** per verificare gli aggiornamenti dell'interfaccia con dati di prova.

> **Nota di collaborazione:** questa attività è sviluppata sul branch personale `feature/SCRUM-111-dashboard`. Finché non viene approvata una Pull Request, non modifica automaticamente il branch principale del progetto del gruppo. La dashboard è una componente in sviluppo, non una funzionalità integrata e conclusa.
