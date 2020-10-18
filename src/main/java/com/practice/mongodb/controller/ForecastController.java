package com.practice.mongodb.controller;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.sevice.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ForecastController {

    private final ForecastService forecastService;

    @Autowired
    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("points/{lat}/{lon}")
    public String getForecast(@PathVariable Double lat, @PathVariable Double lon) {
        String uuid = UUID.randomUUID().toString();
        forecastService.saveForecast(lat, lon, uuid);
        return uuid;
    }

    @GetMapping("forecast/{uuid}")
    public ResponseEntity<Forecast> getForecast(@PathVariable String uuid) {
        Forecast forecast = forecastService.getForecast(uuid);
        if (forecast == null ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecast);
    }
}
