package com.practice.mongodb.sevice;

import com.practice.mongodb.document.Forecast;

public interface ForecastService {

    void saveForecast(double lat, double lon, String uuid);
    Forecast getForecast(String uuid);
}
