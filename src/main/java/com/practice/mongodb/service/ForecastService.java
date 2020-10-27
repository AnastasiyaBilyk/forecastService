package com.practice.mongodb.service;

import com.practice.mongodb.document.Forecast;

import java.util.Set;
import java.util.UUID;

public interface ForecastService {

    void saveForecast(double lat, double lon, UUID uuid, String clientId);
    Forecast getForecast(UUID uuid);
    Set<Forecast> getForecasts(String clientId);
}
