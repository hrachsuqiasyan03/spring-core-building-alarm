package com.epam.training.spring.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private final BuildingEventReader buildingEventReader;

    //Constructor injection
    @Autowired
    public Application(BuildingEventReader buildingEventReader) {
        this.buildingEventReader = buildingEventReader;
    }

    public void run() {
        LOG.info("run() method has been called");

        buildingEventReader.readEvents();
    }
}
