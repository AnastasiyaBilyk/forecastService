package com.practice.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class Forecast {

    @Id
    private String uuid;

    @Indexed(name = "expire_after_index", expireAfter = "30m")
    private LocalDateTime creationDate = LocalDateTime.now();

    private List<Period> periods;

    public Forecast(String uuid, List<Period> periods) {
        this.uuid = uuid;
        this.periods = periods;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

}
