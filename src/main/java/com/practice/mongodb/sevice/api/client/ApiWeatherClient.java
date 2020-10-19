package com.practice.mongodb.sevice.api.client;

import com.practice.mongodb.document.Forecast;

import java.io.IOException;
import java.util.UUID;

public interface ApiWeatherClient {

    Forecast getForecast(UUID uuid, double lat, double lon) throws IOException;
}
