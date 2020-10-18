package com.practice.mongodb.repository;

import com.practice.mongodb.document.Forecast;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ForecastRepository extends MongoRepository<Forecast, String> {
}
