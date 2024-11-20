package com.g.b.rtd.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents a temperature measurement with its associated unit.
 * This class handles temperature values in different scales (Fahrenheit, Celsius, Kelvin).
 */
@Data
public class Temperature {
    @NotNull(message = "Temperature value is required")
    private Double value;

    @NotNull(message = "Temperature unit is required")
    private TemperatureUnit unit;

    /**
     * Gets the formatted temperature string including the unit symbol.
     * @return Formatted temperature string (e.g., "72.5°F")
     */
    public String getFormattedTemperature() {
        return String.format("%.1f%s", value, unit.getSymbol());
    }
}
