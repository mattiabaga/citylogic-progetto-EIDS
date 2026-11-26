
package com.citylogic.controller;

import java.util.Locale;

public final class StatusDashboardFormatter {

    private StatusDashboardFormatter() {
    }

    public static String budget(double value) {
        return String.format(Locale.ITALY,
                "Budget: %,.2f crediti", value);
    }

    public static String population(int value) {
        return String.format(Locale.ITALY,
                "Popolazione: %,d", value);
    }

    public static String happiness(double value) {
        return String.format(Locale.ITALY,
                "Felicità: %.1f%%", value);
    }

    public static String pollution(double value) {
        return String.format(Locale.ITALY,
                "Inquinamento: %.1f", value);
    }

    public static String tick(long value) {
        return String.format(Locale.ITALY,
                "Tick: %,d", value);
    }
}
