package com.practice.mongodb.sevice;

import com.practice.mongodb.document.Forecast;

import java.util.UUID;

public interface ForecastService {

    void saveForecast(double lat, double lon, UUID uuid);
    Forecast getForecast(UUID uuid);
}
