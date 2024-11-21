package com.g.b.rtd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * DTO for weather.gov Alerts endpoint response.
 * Follows GeoJSON format with features containing alert information.
 */
@Data
public class AlertResponse {
    @JsonProperty("@context")
    private Object context;

    @JsonProperty("type")
    private String type;

    @JsonProperty("features")
    private List<AlertFeature> features;

    @JsonProperty("title")
    private String title;

    @JsonProperty("updated")
    private String updated;

    /**
     * Inner class representing a single alert feature in the GeoJSON response.
     */
    @Data
    public static class AlertFeature {
        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        private String type;

        @JsonProperty("geometry")
        private Object geometry;

        @JsonProperty("properties")
        private AlertProperties properties;
    }

    /**
     * Gets the number of active alerts in the response.
     * @return count of active alerts
     */
    public int getActiveAlertCount() {
        if (features == null) {
            return 0;
        }
        return (int) features.stream()
                .filter(feature -> feature.getProperties() != null && feature.getProperties().isActive())
                .count();
    }

    /**
     * Checks if there are any severe or extreme alerts.
     * @return true if any alerts are severe or extreme
     */
    public boolean hasSevereAlerts() {
        if (features == null) {
            return false;
        }
        return features.stream()
                .filter(feature -> feature.getProperties() != null)
                .map(feature -> feature.getProperties().getSeverity())
                .anyMatch(severity -> "SEVERE".equalsIgnoreCase(severity) || 
                                    "EXTREME".equalsIgnoreCase(severity));
    }
}
