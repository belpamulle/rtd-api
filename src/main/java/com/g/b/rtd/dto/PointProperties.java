package com.g.b.rtd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO for weather.gov Points endpoint response properties.
 * Contains metadata about a location, including grid coordinates needed for other endpoints.
 */
@Data
public class PointProperties {
    @JsonProperty("gridId")
    private String gridId;

    @JsonProperty("gridX")
    private Integer gridX;

    @JsonProperty("gridY")
    private Integer gridY;

    @JsonProperty("forecast")
    private String forecastUrl;

    @JsonProperty("forecastHourly")
    private String forecastHourlyUrl;

    @JsonProperty("observationStations")
    private String observationStationsUrl;
}
