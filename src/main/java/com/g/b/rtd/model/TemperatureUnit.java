package com.g.b.rtd.model;

/**
 * Enumeration of supported temperature units.
 */
public enum TemperatureUnit {
    /**
     * Fahrenheit temperature scale
     */
    FAHRENHEIT("°F"),
    
    /**
     * Celsius temperature scale
     */
    CELSIUS("°C"),
    
    /**
     * Kelvin temperature scale
     */
    KELVIN("K");

    private final String symbol;

    TemperatureUnit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
