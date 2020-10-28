package com.practice.mongodb.controller;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ForecastController {

    private final ForecastService forecastService;

    @Autowired
    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("points/{lat}/{lon}")
    public UUID getForecast(@PathVariable Double lat, @PathVariable Double lon) {
        UUID uuid = UUID.randomUUID();
        forecastService.saveForecast(lat, lon, uuid);
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

    @PostMapping("points/{lat}/{lon}")
    public void addToQueue(@PathVariable Double lat, @PathVariable Double lon, @RequestHeader("Client-Id") String clientId) {
        forecastService.sendForecastToQueue(lat, lon, clientId);
    }
}
