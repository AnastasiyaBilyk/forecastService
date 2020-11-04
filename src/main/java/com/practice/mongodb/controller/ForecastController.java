package com.practice.mongodb.controller;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ForecastController {

    private final ForecastService forecastService;
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String consumerBootstrapServer;
    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Autowired
    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("points/{lat}/{lon}")
    public UUID getForecast(@PathVariable Double lat, @PathVariable Double lon, @RequestHeader("Client-Id") String clientId) {
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

    @GetMapping("/config")
    public Map<String, String> getMessageQueueConfiguration(@RequestHeader("Client-Id") String clientId) {
        Map<String, String> configuration = new HashMap<>();
        configuration.put("consumerBootstrapServer", consumerBootstrapServer);
        configuration.put("consumerGroupId", consumerGroupId);
        configuration.put("topic", clientId);
        return configuration;
    }
}
