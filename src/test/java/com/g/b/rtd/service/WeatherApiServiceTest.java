package com.g.b.rtd.service;

import com.g.b.rtd.dto.*;
import com.g.b.rtd.dto.AlertResponse.AlertFeature;
import com.g.b.rtd.exception.WeatherApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherApiServiceTest {

    @Mock
    private WebClient weatherApiClient;
    
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private WeatherApiService weatherApiService;

    @BeforeEach
    void setUp() {
        weatherApiService = new WeatherApiService(weatherApiClient);
    }

    @Nested
    @DisplayName("getPointData Tests")
    class GetPointDataTests {
        
        @Test
        @DisplayName("Should successfully retrieve point data for valid coordinates")
        void shouldRetrievePointData() {
            // Arrange
            double latitude = 40.7128;
            double longitude = -74.0060;
            PointResponse expectedResponse = new PointResponse();
            PointProperties properties = new PointProperties();
            properties.setForecastUrl("https://api.weather.gov/gridpoints/NYC/1,2/forecast");
            expectedResponse.setProperties(properties);

            when(weatherApiClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(PointResponse.class))
                .thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(weatherApiService.getPointData(latitude, longitude))
                .expectNext(expectedResponse)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should throw exception for invalid latitude")
        void shouldThrowExceptionForInvalidLatitude() {
            // Act & Assert
            assertThrows(WeatherApiException.class, 
                () -> weatherApiService.getPointData(91.0, -74.0060).block());
        }

        @Test
        @DisplayName("Should throw exception for invalid longitude")
        void shouldThrowExceptionForInvalidLongitude() {
            // Act & Assert
            assertThrows(WeatherApiException.class, 
                () -> weatherApiService.getPointData(40.7128, 181.0).block());
        }
    }

    @Nested
    @DisplayName("getCurrentForecast Tests")
    class GetCurrentForecastTests {
        
        @Test
        @DisplayName("Should successfully retrieve forecast for valid coordinates")
        void shouldRetrieveForecast() {
            // Arrange
            double latitude = 40.7128;
            double longitude = -74.0060;
            String forecastUrl = "https://api.weather.gov/gridpoints/NYC/1,2/forecast";
            
            PointResponse pointResponse = new PointResponse();
            PointProperties pointProperties = new PointProperties();
            pointProperties.setForecastUrl(forecastUrl);
            pointResponse.setProperties(pointProperties);
            
            ForecastResponse expectedForecast = new ForecastResponse();
            ForecastResponse.ForecastProperties forecastProperties = new ForecastResponse.ForecastProperties();
            ForecastPeriodProperties periodProperties = new ForecastPeriodProperties();
            periodProperties.setTemperature(72);
            forecastProperties.setPeriods(Collections.singletonList(periodProperties));
            expectedForecast.setProperties(forecastProperties);

            // Mock point data call
            when(weatherApiClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(PointResponse.class))
                .thenReturn(Mono.just(pointResponse));

            // Mock forecast call
            when(requestHeadersUriSpec.uri(forecastUrl)).thenReturn(requestHeadersSpec);
            when(responseSpec.bodyToMono(ForecastResponse.class))
                .thenReturn(Mono.just(expectedForecast));

            // Act & Assert
            StepVerifier.create(weatherApiService.getCurrentForecast(latitude, longitude))
                .expectNext(expectedForecast)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should handle null forecast URL")
        void shouldHandleNullForecastUrl() {
            // Arrange
            double latitude = 40.7128;
            double longitude = -74.0060;
            
            PointResponse pointResponse = new PointResponse();
            PointProperties pointProperties = new PointProperties();
            pointProperties.setForecastUrl(null);
            pointResponse.setProperties(pointProperties);

            when(weatherApiClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(PointResponse.class))
                .thenReturn(Mono.just(pointResponse));

            // Act & Assert
            StepVerifier.create(weatherApiService.getCurrentForecast(latitude, longitude))
                .expectError(NullPointerException.class)
                .verify();
        }
    }

    @Nested
    @DisplayName("getActiveAlerts Tests")
    class GetActiveAlertsTests {
        
        @Test
        @DisplayName("Should successfully retrieve active alerts for valid coordinates")
        void shouldRetrieveActiveAlerts() {
            // Arrange
            double latitude = 40.7128;
            double longitude = -74.0060;
            
            AlertResponse expectedResponse = new AlertResponse();
            AlertFeature alertFeature = new AlertFeature();
            AlertProperties alertProperties = new AlertProperties();
            
            // Set alert times to make it active
            ZonedDateTime now = ZonedDateTime.now();
            alertProperties.setEffective(now.minusHours(1));
            alertProperties.setExpires(now.plusHours(1));
            alertProperties.setSeverity("SEVERE");
            
            alertFeature.setProperties(alertProperties);
            expectedResponse.setFeatures(Collections.singletonList(alertFeature));

            when(weatherApiClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(AlertResponse.class))
                .thenReturn(Mono.just(expectedResponse));

            // Act & Assert
            StepVerifier.create(weatherApiService.getActiveAlerts(latitude, longitude))
                .expectNext(expectedResponse)
                .verifyComplete();
        }

        @Test
        @DisplayName("Should throw exception for invalid coordinates")
        void shouldThrowExceptionForInvalidCoordinates() {
            // Act & Assert
            assertThrows(WeatherApiException.class, 
                () -> weatherApiService.getActiveAlerts(95.0, -74.0060).block());
        }

        @Test
        @DisplayName("Should handle empty alerts response")
        void shouldHandleEmptyAlertsResponse() {
            // Arrange
            double latitude = 40.7128;
            double longitude = -74.0060;
            AlertResponse emptyResponse = new AlertResponse();
            emptyResponse.setFeatures(Collections.emptyList());

            when(weatherApiClient.get()).thenReturn(requestHeadersUriSpec);
            when(requestHeadersUriSpec.uri(any(java.util.function.Function.class)))
                .thenReturn(requestHeadersSpec);
            when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(AlertResponse.class))
                .thenReturn(Mono.just(emptyResponse));

            // Act & Assert
            StepVerifier.create(weatherApiService.getActiveAlerts(latitude, longitude))
                .expectNext(emptyResponse)
                .verifyComplete();
        }
    }
}
