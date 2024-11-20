package com.g.b.rtd.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.ZonedDateTime;

/**
 * Represents a weather alert or warning issued by the National Weather Service.
 */
@Data
public class WeatherAlert {
    @NotBlank(message = "Alert ID is required")
    private String id;

    @NotBlank(message = "Alert type is required")
    private String alertType;

    @NotBlank(message = "Alert headline is required")
    private String headline;

    @NotBlank(message = "Alert description is required")
    private String description;

    @NotNull(message = "Alert severity is required")
    private AlertSeverity severity;

    @NotNull(message = "Alert start time is required")
    private ZonedDateTime startTime;

    @NotNull(message = "Alert end time is required")
    private ZonedDateTime endTime;

    private String area;

    /**
     * Enumeration of possible alert severity levels.
     */
    public enum AlertSeverity {
        EXTREME,    // Extraordinary threat to life or property
        SEVERE,     // Significant threat to life or property
        MODERATE,   // Possible threat to life or property
        MINOR,      // Minimal or no known threat to life or property
        UNKNOWN     // Unknown threat level
    }

    /**
     * Checks if the alert is currently active.
     * @return true if the alert is currently active, false otherwise
     */
    public boolean isActive() {
        ZonedDateTime now = ZonedDateTime.now();
        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }
}
