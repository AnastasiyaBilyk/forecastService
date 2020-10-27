package com.practice.mongodb.controller;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
public class ForecastController {

    private final ForecastService forecastService;

    @Autowired
    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("points/{lat}/{lon}")
    public UUID getForecast(@PathVariable Double lat, @PathVariable Double lon, @RequestParam(value = "clientId") String clientId) {
        UUID uuid = UUID.randomUUID();
        forecastService.saveForecast(lat, lon, uuid, clientId);
        return uuid;
    }

    @GetMapping("forecast/{uuid}")
    public ResponseEntity<Forecast> getForecast(@PathVariable String uuid) {
        Forecast forecast = forecastService.getForecast(UUID.fromString(uuid));
        if (forecast == null ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/subscribe")
    public Set<Forecast> subscribe(@RequestParam(value = "clientId") String clientId) {
        while (true) {
            Set<Forecast> forecasts = forecastService.getForecasts(clientId);
            if (!forecasts.isEmpty()) {
                return forecasts;
            }
        }
    }
}
