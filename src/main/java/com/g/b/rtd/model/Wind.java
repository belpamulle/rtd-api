package com.g.b.rtd.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Represents wind conditions including speed, direction, and gusts.
 */
@Data
public class Wind {
    @NotNull(message = "Wind speed is required")
    @DecimalMin(value = "0.0", message = "Wind speed must be non-negative")
    private Double speed;

    @NotNull(message = "Wind direction in degrees is required")
    @Min(value = 0, message = "Wind direction must be between 0 and 360 degrees")
    @Max(value = 360, message = "Wind direction must be between 0 and 360 degrees")
    private Integer directionDegrees;

    @DecimalMin(value = "0.0", message = "Wind gust speed must be non-negative")
    private Double gustSpeed;

    /**
     * Unit of measurement for wind speed (e.g., mph, km/h)
     */
    @NotNull(message = "Wind speed unit is required")
    private String speedUnit;

    /**
     * Gets the cardinal direction (N, NE, E, etc.) from degrees
     * @return Cardinal direction as a string
     */
    public String getCardinalDirection() {
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                             "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = (int) Math.round(((directionDegrees % 360) / 22.5));
        return directions[index % 16];
    }
}
