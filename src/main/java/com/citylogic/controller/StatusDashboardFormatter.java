
package com.citylogic.controller;

import java.util.Locale;

/**
 * Formattazione delle metriche mostrate nella dashboard, secondo Locale.ITALY
 * (punto per le migliaia, virgola per i decimali).
 *
 * <p>Restituisce solo il valore: il nome della metrica è già scritto
 * nell'etichetta sopra il valore (vedi status-dashboard.fxml).
 * Classe senza dipendenze da JavaFX, quindi testabile con JUnit.</p>
 */
public final class StatusDashboardFormatter {

    private StatusDashboardFormatter() {
    }

    /** Es. 12500 → "12.500,00 crediti". */
    public static String budget(double value) {
        return String.format(Locale.ITALY,
                "%,.2f crediti", value);
    }

    /** Es. 1250 → "1.250". */
    public static String population(int value) {
        return String.format(Locale.ITALY,
                "%,d", value);
    }

    /** Es. 74.5 → "74,5%". */
    public static String happiness(double value) {
        return String.format(Locale.ITALY,
                "%.1f%%", value);
    }

    /** Es. 32 → "32,0" (non mostrato nella barra, utile per grafici e report). */
    public static String pollution(double value) {
        return String.format(Locale.ITALY,
                "%.1f", value);
    }

    /** Es. 1000 → "1.000". */
    public static String tick(long value) {
        return String.format(Locale.ITALY,
                "%,d", value);
    }
}
