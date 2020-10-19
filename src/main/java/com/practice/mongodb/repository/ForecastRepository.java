package com.practice.mongodb.repository;

import com.practice.mongodb.document.Forecast;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ForecastRepository extends MongoRepository<Forecast, UUID> {
}
