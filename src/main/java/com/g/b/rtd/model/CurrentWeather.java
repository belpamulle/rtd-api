package com.g.b.rtd.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.ZonedDateTime;

/**
 * Represents current weather conditions at a specific location.
 */
@Data
public class CurrentWeather {
    @NotNull(message = "Observation time is required")
    private ZonedDateTime observationTime;

    @Valid
    @NotNull(message = "Temperature is required")
    private Temperature temperature;

    @Valid
    @NotNull(message = "Wind information is required")
    private Wind wind;

    @NotBlank(message = "Weather description is required")
    private String description;

    @NotNull(message = "Relative humidity is required")
    private Integer relativeHumidity;

    private Double precipitationLastHour;

    @NotNull(message = "Cloud cover percentage is required")
    private Integer cloudCoverPercentage;

    @Valid
    @NotNull(message = "Location coordinates are required")
    private Coordinates coordinates;

    @NotBlank(message = "Station ID is required")
    private String stationId;

    private String visibility;

    @NotNull(message = "Barometric pressure is required")
    private Double barometricPressure;

    private String pressureUnit;

    /**
     * Checks if precipitation was recorded in the last hour.
     * @return true if precipitation was recorded, false otherwise
     */
    public boolean hasPrecipitation() {
        return precipitationLastHour != null && precipitationLastHour > 0;
    }

    /**
     * Determines if current conditions are considered clear.
     * @return true if cloud cover is less than 30%, false otherwise
     */
    public boolean isClear() {
        return cloudCoverPercentage < 30;
    }
}
