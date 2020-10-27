package com.practice.mongodb.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document
@Data
public class Forecast {

    @Id
    private UUID uuid;

    @Indexed(name = "expire_after_index", expireAfter = "30m")
    private LocalDateTime creationDate = LocalDateTime.now();

    private List<Period> periods;

    public Forecast() {}

    public Forecast(UUID uuid, List<Period> periods) {
        this.uuid = uuid;
        this.periods = periods;
    }
}
