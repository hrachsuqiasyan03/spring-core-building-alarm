package com.epam.training.spring.beans;

import com.epam.training.spring.model.BuildingEvent;
import com.epam.training.spring.model.EventDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class BuildingEventReader implements ApplicationEventPublisherAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildingEventReader.class);
    private static final TypeReference<List<EventDto>> LIST_OF_EVENTS_TYPE_REF = new TypeReference<>() {};

    private ApplicationEventPublisher applicationEventPublisher;
    private String eventFilePath;
    private File eventFile;

    private ObjectMapper objectMapper;

    public BuildingEventReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        objectMapper.findAndRegisterModules();
    }

   @Value("${event.file.path}")
    public void setEventFilePath(String eventFilePath) {
        this.eventFilePath = eventFilePath;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;

    }

    @PostConstruct
    public void checkInputFile() {
        LOGGER.info("EventReader.checkInputFile() has been called");
        eventFile = new File(eventFilePath);
        if (!eventFile.exists()) {
            throw new RuntimeException("File does not exists:" + eventFile.getAbsolutePath());
        }
    }

    public void readEvents() {
        LOGGER.info("Reading and publishing events...");
        try {
            List<EventDto> events = objectMapper.readValue(eventFile, LIST_OF_EVENTS_TYPE_REF);
            for (EventDto event : events) {
                BuildingEvent buildingEvent = createEvent(event);
                applicationEventPublisher.publishEvent(buildingEvent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BuildingEvent createEvent(EventDto event) {
        BuildingEvent buildingEvent = new BuildingEvent(this);
        buildingEvent.setEventType(event.getEventType());
        buildingEvent.setTime(event.getTime());
        buildingEvent.setLocation(event.getLocation());
        buildingEvent.setMessage(event.getMessage());
        return buildingEvent;
    }
}
