package com.g.b.rtd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.ZonedDateTime;

/**
 * DTO for a single forecast period from the weather.gov API.
 * Contains detailed weather information for a specific time period.
 */
@Data
public class ForecastPeriodProperties {
    @JsonProperty("number")
    private Integer number;

    @JsonProperty("name")
    private String name;

    @JsonProperty("startTime")
    private ZonedDateTime startTime;

    @JsonProperty("endTime")
    private ZonedDateTime endTime;

    @JsonProperty("temperature")
    private Integer temperature;

    @JsonProperty("temperatureUnit")
    private String temperatureUnit;

    @JsonProperty("temperatureTrend")
    private String temperatureTrend;

    @JsonProperty("windSpeed")
    private String windSpeed;

    @JsonProperty("windDirection")
    private String windDirection;

    @JsonProperty("icon")
    private String icon;

    @JsonProperty("shortForecast")
    private String shortForecast;

    @JsonProperty("detailedForecast")
    private String detailedForecast;

    @JsonProperty("isDaytime")
    private Boolean isDaytime;

    @JsonProperty("probabilityOfPrecipitation")
    private Object probabilityOfPrecipitation;

    @JsonProperty("relativeHumidity")
    private Object relativeHumidity;
}
