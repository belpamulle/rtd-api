package com.g.b.rtd.service;

import com.g.b.rtd.dto.*;
import com.g.b.rtd.exception.WeatherApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Objects;

/**
 * Service for interacting with the Weather.gov API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherApiService {

    private final WebClient weatherApiClient;

    /**
     * Gets point data for the given coordinates.
     * This is required to get the grid coordinates for other API calls.
     */
    public Mono<PointResponse> getPointData(double latitude, double longitude) {
        validateCoordinates(latitude, longitude);
        
        String path = String.format("/points/%f,%f", latitude, longitude);
        log.debug("Fetching point data from: {}", path);
        
        return weatherApiClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(PointResponse.class)
                .doOnSuccess(response -> log.debug("Successfully retrieved point data for coordinates: {},{}", 
                        latitude, longitude))
                .doOnError(error -> log.error("Error fetching point data for coordinates: {},{}", 
                        latitude, longitude, error));
    }

    /**
     * Gets the current forecast for the given coordinates.
     */
    public Mono<ForecastResponse> getCurrentForecast(double latitude, double longitude) {
        return getPointData(latitude, longitude)
                .flatMap(pointResponse -> {
                    String forecastUrl = Objects.requireNonNull(
                            pointResponse.getProperties().getForecastUrl(),
                            "Forecast URL not found in point data"
                    );
                    
                    log.debug("Fetching forecast from: {}", forecastUrl);
                    return weatherApiClient.get()
                            .uri(forecastUrl)
                            .retrieve()
                            .bodyToMono(ForecastResponse.class);
                })
                .doOnSuccess(response -> log.debug("Successfully retrieved forecast for coordinates: {},{}", 
                        latitude, longitude))
                .doOnError(error -> log.error("Error fetching forecast for coordinates: {},{}", 
                        latitude, longitude, error));
    }

    /**
     * Gets active alerts for the given coordinates.
     */
    public Mono<AlertResponse> getActiveAlerts(double latitude, double longitude) {
        validateCoordinates(latitude, longitude);
        
        String path = "/alerts/active";
        log.debug("Fetching active alerts for coordinates: {},{}", latitude, longitude);
        
        return weatherApiClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("point", String.format("%f,%f", latitude, longitude))
                        .build())
                .retrieve()
                .bodyToMono(AlertResponse.class)
                .doOnSuccess(response -> log.debug("Successfully retrieved {} active alerts", 
                        response.getActiveAlertCount()))
                .doOnError(error -> log.error("Error fetching active alerts for coordinates: {},{}", 
                        latitude, longitude, error));
    }

    /**
     * Validates the given coordinates.
     * @throws WeatherApiException if coordinates are invalid
     */
    private void validateCoordinates(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new WeatherApiException(
                    "Invalid latitude. Must be between -90 and 90",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (longitude < -180 || longitude > 180) {
            throw new WeatherApiException(
                    "Invalid longitude. Must be between -180 and 180",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
