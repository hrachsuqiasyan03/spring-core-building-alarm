package com.epam.training.spring.beans;

import com.epam.training.spring.model.Alarm;
import com.epam.training.spring.model.BuildingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BuildingEventHandler implements ApplicationListener<BuildingEvent> {

    private final AlarmHandler alarmHandler;

    public BuildingEventHandler(AlarmHandler alarmHandler) {
        this.alarmHandler = alarmHandler;
    }
    @Override
    public void onApplicationEvent(BuildingEvent event) {
        Alarm alarm = switch (event.getEventType ()) {
            case TEMPERATURE_TOO_HIGH -> new Alarm ("Call Mechanic", event.getLocation(), "Room is hot");
            case FIRE_DETECTION -> new Alarm ("Call Firefighters", event.getLocation(), "Fire detected");
            default -> null;
        };

        if (alarm != null) {
            alarmHandler.handleAlarm(alarm);
        }

    }

}
