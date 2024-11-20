package com.g.b.rtd.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for handling Weather API specific errors.
 */
@Getter
public class WeatherApiException extends RuntimeException {
    private final HttpStatus status;
    private final String type;
    private final String detail;
    private final String correlationId;

    public WeatherApiException(String message, HttpStatus status, String type, String detail, String correlationId) {
        super(message);
        this.status = status;
        this.type = type;
        this.detail = detail;
        this.correlationId = correlationId;
    }

    public WeatherApiException(String message, HttpStatus status) {
        this(message, status, null, null, null);
    }

    public WeatherApiException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
        this.type = null;
        this.detail = cause.getMessage();
        this.correlationId = null;
    }

    /**
     * Creates an exception from the Weather.gov API error response.
     */
    public static WeatherApiException fromApiError(WeatherApiError error, HttpStatus status) {
        return new WeatherApiException(
            error.getTitle(),
            status,
            error.getType(),
            error.getDetail(),
            error.getCorrelationId()
        );
    }

    /**
     * DTO for Weather.gov API error responses.
     */
    @Getter
    public static class WeatherApiError {
        private String type;
        private String title;
        private String detail;
        private String correlationId;
    }
}
