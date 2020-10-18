package com.practice.mongodb.sevice;

public interface ForecastService {

    void saveForecast(double lat, double lon, String uuid);
}
