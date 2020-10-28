package com.practice.mongodb.service;

import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.repository.ForecastRepository;
import com.practice.mongodb.service.api.client.ApiWeatherClient;
import com.practice.mongodb.service.messaging.MessagingService;
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
    private final MessagingService<String, Forecast> kafkaMessagingService;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository,
                               ExecutorService executorService,
                               ApiWeatherClient apiWeatherClient,
                               MessagingService<String, Forecast> kafkaMessagingService) {
        this.forecastRepository = forecastRepository;
        this.executorService = executorService;
        this.apiWeatherClient = apiWeatherClient;
        this.kafkaMessagingService = kafkaMessagingService;
    }

    @Override
    public void saveForecast(double lat, double lon, UUID uuid, String clientId) {
        executorService.submit(() -> {
            try {
                Forecast forecast = apiWeatherClient.getForecast(uuid, lat, lon);
                forecastRepository.save(forecast);
                kafkaMessagingService.sendToQueue(clientId, forecast);
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
