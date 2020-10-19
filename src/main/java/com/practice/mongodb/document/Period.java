package com.practice.mongodb.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Period {
    int number;
    String name;
    String startTime;
    String endTime;
    @JsonProperty(value = "isDaytime")
    boolean isDaytime;
    int temperature;
    String temperatureUnit;
    String temperatureTrend;
    String windSpeed;
    String windDirection;
    String icon;
    String shortForecast;
    String detailedForecast;
}
