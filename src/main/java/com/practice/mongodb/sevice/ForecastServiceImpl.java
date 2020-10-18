package com.practice.mongodb.sevice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.practice.mongodb.document.Forecast;
import com.practice.mongodb.document.Period;
import com.practice.mongodb.repository.ForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;

    private final ExecutorService executorService;

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${api.weather.host}")
    private String apiWeatherHost;
    @Value("${api.weather.path}")
    private String apiWeatherPath;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, ExecutorService executorService, RestTemplate restTemplate) {
        this.forecastRepository = forecastRepository;
        this.executorService = executorService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void saveForecast(double lat, double lon, String uuid) {
        executorService.submit(() -> {
            try {
                List<Period> periods = getPeriods(getForecastURI(lat, lon));
                Forecast forecast =  new Forecast(uuid, periods);
                forecastRepository.save(forecast);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getForecastURI(double lat, double lon) throws JsonProcessingException {
        URI uri = new DefaultUriBuilderFactory()
                .builder()
                .scheme("https")
                .host(apiWeatherHost)
                .path(apiWeatherPath + lat + "," + lon)
                .build();
        ResponseEntity<String> response = restTemplate.getForEntity(uri,String.class);
        JsonNode root = mapper.readTree(response.getBody());
        return root.findValue("forecast").textValue();
    }

    private List<Period> getPeriods(String forecastURI) throws IOException {
        ResponseEntity<String> forecastResponse = restTemplate.getForEntity(forecastURI,String.class);
        JsonNode forecastRoot = mapper.readTree(forecastResponse.getBody());
        JsonNode periodsNode = forecastRoot.findValue("periods");
        ObjectReader reader = mapper.readerFor(new TypeReference<List<Period>>() {
        });
        return reader.readValue(periodsNode);
    }

}
