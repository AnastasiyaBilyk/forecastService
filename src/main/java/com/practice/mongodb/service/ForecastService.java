package com.practice.mongodb.service;

import com.practice.mongodb.document.Forecast;

import java.util.UUID;

public interface ForecastService {

    void saveForecast(double lat, double lon, UUID uuid);
    Forecast getForecast(UUID uuid);
}
