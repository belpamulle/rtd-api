package com.g.b.rtd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * DTO for weather.gov Gridpoint Forecast endpoint response.
 * Follows GeoJSON format with properties containing the forecast periods.
 */
@Data
public class ForecastResponse {
    @JsonProperty("@context")
    private Object context;

    @JsonProperty("type")
    private String type;

    @JsonProperty("geometry")
    private Object geometry;

    @JsonProperty("properties")
    private ForecastProperties properties;

    /**
     * Inner class representing the properties object of the forecast response.
     */
    @Data
    public static class ForecastProperties {
        @JsonProperty("updated")
        private String updated;

        @JsonProperty("units")
        private String units;

        @JsonProperty("forecastGenerator")
        private String forecastGenerator;

        @JsonProperty("generatedAt")
        private String generatedAt;

        @JsonProperty("updateTime")
        private String updateTime;

        @JsonProperty("validTimes")
        private String validTimes;

        @JsonProperty("periods")
        private List<ForecastPeriodProperties> periods;
    }
}
