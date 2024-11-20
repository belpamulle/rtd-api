package com.g.b.rtd.config;

import com.g.b.rtd.exception.WeatherApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;

/**
 * Configuration for WebClient to interact with the Weather.gov API.
 */
@Configuration
public class WebClientConfig {

    @Value("${weather.api.base-url:https://api.weather.gov}")
    private String baseUrl;

    @Value("${weather.api.user-agent:RealTimeData/1.0 (test.user@gmail.com)}")
    private String userAgent;

    @Value("${weather.api.timeout:30}")
    private int timeoutSeconds;

    /**
     * Creates and configures the WebClient bean for Weather.gov API.
     */
    @Bean
    public WebClient weatherApiClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(errorHandler())
                .filter(timeoutHandler())
                .build();
    }

    /**
     * Creates an error handling filter for the WebClient.
     */
    private ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(WeatherApiException.WeatherApiError.class)
                        .defaultIfEmpty(new WeatherApiException.WeatherApiError())
                        .flatMap(error -> {
                            HttpStatus status = HttpStatus.valueOf(clientResponse.statusCode().value());
                            WeatherApiException exception = error.getType() != null ?
                                    WeatherApiException.fromApiError(error, status) :
                                    new WeatherApiException("Error response from Weather API", status);
                            return Mono.<ClientResponse>error(exception);
                        });
            }
            return Mono.just(clientResponse);
        });
    }

    /**
     * Creates a timeout handling filter for the WebClient.
     */
    private ExchangeFilterFunction timeoutHandler() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> 
            Mono.just(request)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .onErrorMap(ex -> new WeatherApiException(
                    "Request timed out after " + timeoutSeconds + " seconds",
                    ex,
                    HttpStatus.GATEWAY_TIMEOUT
                ))
        );
    }
}
