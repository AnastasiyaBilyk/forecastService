package com.practice.mongodb.service.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.document.Period;
import com.practice.mongodb.service.helper.ForecastJsonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Component
public class ApiWeatherClientImpl implements ApiWeatherClient {

    private final RestTemplate restTemplate;

    private final ForecastJsonProcessor forecastJsonProcessor;

    @Value("${api.weather.host}")
    private String apiWeatherHost;
    @Value("${api.weather.path}")
    private String apiWeatherPath;

    @Autowired
    public ApiWeatherClientImpl(RestTemplate restTemplate, ForecastJsonProcessor forecastJsonProcessor) {
        this.restTemplate = restTemplate;
        this.forecastJsonProcessor = forecastJsonProcessor;
    }

    @Override
    public Forecast getForecast(UUID uuid, double lat, double lon) throws IOException {
        List<Period> periods = getPeriods(lat, lon);
        return new Forecast(uuid, periods);
    }

    private List<Period> getPeriods(double lat, double lon) throws IOException {
        ResponseEntity<String> forecastResponse = restTemplate.getForEntity(getForecastURI(lat, lon),String.class);
        return forecastJsonProcessor.getPeriods(forecastResponse);
    }

    private String getForecastURI(double lat, double lon) throws JsonProcessingException {
        URI uri = buildApiWeatherURI(lat, lon);
        ResponseEntity<String> response = restTemplate.getForEntity(uri,String.class);
        return forecastJsonProcessor.getForecastURI(response);
    }

    private URI buildApiWeatherURI(double lat, double lon) {
        return new DefaultUriBuilderFactory()
                .builder()
                .scheme("https")
                .host(apiWeatherHost)
                .path(apiWeatherPath + lat + "," + lon)
                .build();
    }
}
