package com.g.b.rtd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO for weather.gov Points endpoint response.
 * Follows GeoJSON format with properties containing the grid information.
 */
@Data
public class PointResponse {
    @JsonProperty("@context")
    private Object context;

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("geometry")
    private Object geometry;

    @JsonProperty("properties")
    private PointProperties properties;
}
