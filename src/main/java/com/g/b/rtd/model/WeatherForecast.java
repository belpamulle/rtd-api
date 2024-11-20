package com.g.b.rtd.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Represents a weather forecast containing multiple forecast periods.
 * Can represent either hourly or daily forecasts.
 */
@Data
public class WeatherForecast {
    @NotNull(message = "Forecast type is required")
    private ForecastType type;

    @NotNull(message = "Generated time is required")
    private ZonedDateTime generatedTime;

    @Valid
    @NotNull(message = "Location coordinates are required")
    private Coordinates coordinates;

    @Valid
    @NotEmpty(message = "Forecast periods list cannot be empty")
    private List<ForecastPeriod> periods;

    @NotNull(message = "Update time is required")
    private ZonedDateTime nextUpdate;

    /**
     * Enumeration of forecast types.
     */
    public enum ForecastType {
        HOURLY,     // Hour-by-hour forecast
        DAILY       // Day-by-day forecast
    }

    /**
     * Gets the total forecast duration in hours.
     * @return the total duration in hours
     */
    public long getTotalForecastHours() {
        if (periods == null || periods.isEmpty()) {
            return 0;
        }
        ZonedDateTime start = periods.get(0).getStartTime();
        ZonedDateTime end = periods.get(periods.size() - 1).getEndTime();
        return java.time.Duration.between(start, end).toHours();
    }

    /**
     * Gets the number of forecast periods.
     * @return the number of periods in this forecast
     */
    public int getNumberOfPeriods() {
        return periods != null ? periods.size() : 0;
    }

    /**
     * Checks if this forecast needs updating based on the nextUpdate time.
     * @return true if the forecast needs updating, false otherwise
     */
    public boolean needsUpdate() {
        return ZonedDateTime.now().isAfter(nextUpdate);
    }
}
