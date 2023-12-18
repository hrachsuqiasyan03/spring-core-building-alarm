package com.epam.training.spring.model;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EventDto {
    private EventType eventType;
    private OffsetDateTime time;
    private String location;
    private String message;
}
