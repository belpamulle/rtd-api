package com.g.b.rtd.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.ZonedDateTime;

/**
 * Represents a single forecast period, which can be either hourly or daily.
 */
@Data
public class ForecastPeriod {
    @NotNull(message = "Start time is required")
    private ZonedDateTime startTime;

    @NotNull(message = "End time is required")
    private ZonedDateTime endTime;

    @NotBlank(message = "Forecast description is required")
    private String description;

    @Valid
    @NotNull(message = "Temperature information is required")
    private Temperature temperature;

    @Valid
    @NotNull(message = "Wind information is required")
    private Wind wind;

    @NotNull(message = "Precipitation probability is required")
    private Integer precipitationProbability;

    @NotNull(message = "Relative humidity is required")
    private Integer relativeHumidity;

    private Double precipitationAmount;
    private String precipitationUnit;

    @NotNull(message = "Cloud cover percentage is required")
    private Integer cloudCoverPercentage;

    /**
     * Checks if this is a daytime period based on the start time hour.
     * @return true if this is a daytime period, false otherwise
     */
    public boolean isDaytime() {
        int hour = startTime.getHour();
        return hour >= 6 && hour < 20;
    }

    /**
     * Gets the duration of this forecast period in hours.
     * @return the duration in hours
     */
    public long getDurationHours() {
        return java.time.Duration.between(startTime, endTime).toHours();
    }
}
