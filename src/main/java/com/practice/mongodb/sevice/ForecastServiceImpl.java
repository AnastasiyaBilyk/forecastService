package com.practice.mongodb.sevice;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.repository.ForecastRepository;
import com.practice.mongodb.sevice.api.client.ApiWeatherClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;

    private final ExecutorService executorService;

    private final ApiWeatherClient apiWeatherClient;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, ExecutorService executorService, ApiWeatherClient apiWeatherClient) {
        this.forecastRepository = forecastRepository;
        this.executorService = executorService;
        this.apiWeatherClient = apiWeatherClient;
    }

    @Override
    public void saveForecast(double lat, double lon, UUID uuid) {
        executorService.submit(() -> {
            try {
                forecastRepository.save(apiWeatherClient.getForecast(uuid, lat, lon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Forecast getForecast(UUID uuid) {
        return forecastRepository.findById(uuid).orElse(null);
    }

    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdown();
    }
}
