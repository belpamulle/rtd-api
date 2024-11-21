# Weather.gov API Technical Reference

## Base URL
```
https://api.weather.gov
```

## Required Headers
- **User-Agent**: Required for all requests
  - Purpose: To identify applications and control abusive behavior
  - Recommendation: Include application identifier and contact email

## Core Endpoints

### 1. Points Endpoint
```
GET /points/{latitude},{longitude}
```
- **Purpose**: Get metadata about a location, including grid coordinates needed for other endpoints
- **Response Format**: application/geo+json
- **Key Response Properties**:
  ```json
  {
    "properties": {
      "gridId": "string",      // WFO identifier
      "gridX": integer,        // Grid X coordinate
      "gridY": integer,        // Grid Y coordinate
      "forecast": "string",    // URI for forecast endpoint
      "forecastHourly": "string", 
      "observationStations": "string"
    }
  }
  ```

### 2. Gridpoint Forecast
```
GET /gridpoints/{wfo}/{x},{y}/forecast
```
- **Parameters**:
  - wfo: Forecast office ID
  - x: Grid X coordinate
  - y: Grid Y coordinate
- **Query Parameters**:
  - units: "us" | "si" (default: "us")
- **Response Format**: application/geo+json
- **Key Response Properties**:
  ```json
  {
    "properties": {
      "periods": [
        {
          "number": integer,
          "name": "string",
          "startTime": "date-time",
          "endTime": "date-time",
          "temperature": integer,
          "temperatureUnit": "F" | "C",
          "windSpeed": "string",
          "windDirection": "string",
          "shortForecast": "string",
          "detailedForecast": "string"
        }
      ]
    }
  }
  ```

### 3. Active Alerts
```
GET /alerts/active
```
- **Query Parameters**:
  - point: "latitude,longitude"
  - status
  - message_type
  - urgency
  - severity
  - certainty
- **Response Format**: application/geo+json
- **Key Response Properties**:
  ```json
  {
    "features": [
      {
        "properties": {
          "id": "string",
          "areaDesc": "string",
          "sent": "date-time",
          "effective": "date-time",
          "onset": "date-time",
          "expires": "date-time",
          "status": "string",
          "messageType": "string",
          "severity": "string",
          "urgency": "string",
          "certainty": "string",
          "event": "string",
          "headline": "string",
          "description": "string",
          "instruction": "string"
        }
      }
    ]
  }
  ```

## Error Responses
- Format: application/problem+json
- Structure:
  ```json
  {
    "type": "string",
    "title": "string",
    "status": number,
    "detail": "string",
    "instance": "string",
    "correlationId": "string"
  }
  ```

## Important Enums

### Alert Severity
- Extreme
- Severe
- Moderate
- Minor
- Unknown

### Alert Urgency
- Immediate
- Expected
- Future
- Past
- Unknown

### Alert Certainty
- Observed
- Likely
- Possible
- Unlikely
- Unknown

## Rate Limiting
- No explicit rate limits mentioned in spec
- Should implement reasonable request throttling
- Include proper error handling for 429 responses

## Best Practices
1. Cache Point/Grid data when possible to reduce API calls
2. Implement exponential backoff for retries
3. Use proper error handling for all API responses
4. Include comprehensive request timeout handling
5. Validate coordinate inputs before making API calls