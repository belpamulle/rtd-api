package com.g.b.rtd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.ZonedDateTime;

/**
 * DTO for weather.gov Alert properties.
 * Contains detailed information about a weather alert.
 */
@Data
public class AlertProperties {
    @JsonProperty("id")
    private String id;

    @JsonProperty("areaDesc")
    private String areaDesc;

    @JsonProperty("sent")
    private ZonedDateTime sent;

    @JsonProperty("effective")
    private ZonedDateTime effective;

    @JsonProperty("onset")
    private ZonedDateTime onset;

    @JsonProperty("expires")
    private ZonedDateTime expires;

    @JsonProperty("status")
    private String status;

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("severity")
    private String severity;

    @JsonProperty("urgency")
    private String urgency;

    @JsonProperty("certainty")
    private String certainty;

    @JsonProperty("event")
    private String event;

    @JsonProperty("headline")
    private String headline;

    @JsonProperty("description")
    private String description;

    @JsonProperty("instruction")
    private String instruction;

    @JsonProperty("response")
    private String response;

    @JsonProperty("parameters")
    private Object parameters;

    /**
     * Checks if the alert is currently active.
     * @return true if the alert is currently active based on effective and expiry times
     */
    public boolean isActive() {
        ZonedDateTime now = ZonedDateTime.now();
        return !now.isBefore(effective) && !now.isAfter(expires);
    }

    /**
     * Gets the alert severity level.
     * @return normalized severity level
     */
    public String getNormalizedSeverity() {
        return severity != null ? severity.toUpperCase() : "UNKNOWN";
    }
}
