package com.epam.training.spring.beans;

import com.epam.training.spring.model.BuildingEvent;
import com.epam.training.spring.model.EventDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BuildingEventReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildingEventReader.class);
    private static final TypeReference<List<EventDto>> LIST_OF_EVENTS_TYPE_REF = new TypeReference<>() {};

    private final File eventFile;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BuildingEventReader(String eventFilePath) {
        eventFile = new File(eventFilePath);
        objectMapper.findAndRegisterModules();
    }

    public void readEvents() {
        LOGGER.info("Reading and publishing events...");
        try {
            List<EventDto> events = objectMapper.readValue(eventFile, LIST_OF_EVENTS_TYPE_REF);
            for (EventDto event : events) {
                BuildingEvent buildingEvent = createEvent(event);
                // TODO: publish the events here
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
