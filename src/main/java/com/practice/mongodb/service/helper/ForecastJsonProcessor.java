package com.practice.mongodb.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.practice.mongodb.document.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;;
import java.util.List;

@Component
public class ForecastJsonProcessor {

    private final ObjectMapper mapper;

    @Autowired
    public ForecastJsonProcessor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String getForecastURI(ResponseEntity<String> response) throws JsonProcessingException {
        JsonNode root = mapper.readTree(response.getBody());
        return root.findValue("forecast").textValue();
    }

    public List<Period> getPeriods(ResponseEntity<String> response) throws IOException {
        JsonNode forecastRoot = mapper.readTree(response.getBody());
        JsonNode periodsNode = forecastRoot.findValue("periods");
        ObjectReader reader = mapper.readerFor(new TypeReference<List<Period>>() {});
        return reader.readValue(periodsNode);
    }
}
