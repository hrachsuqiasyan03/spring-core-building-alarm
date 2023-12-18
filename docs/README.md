# Introduction

The goal of this exercise is to apply? some Spring Core framework features that are
not very often used in an average spring application, but good? to understand and practice for a java developer.


# Problem Domain

Buildings are equipped by sensors that send notification to a computer system.
The running application evaluates the event, and if it indicates some danger?, it automatically mitigates the problem by sending alerts.

Ideally the application should process streamed events, nut for simplicity, it reads a json input file, reads avant and processes them.

Example event:

    {
        "eventType": "TEMPERATURE_TOO_HIGH",
        "time": "2023-12-15T12:30:00+01:00",
        "location": "Guest Apartment",
        "message": "28°C"
    }

Depending on the event type, the application sends an alarm that has 3 attributes:

- action
- location
- reason

Note: format of the alert file is a simple text file, each alert is printed in a separate line.

After processing the above event, the system will send an alarm with the following values:
("Call Mechanic", "Guest Apartment", "Room is hot")


# Spring Technologies in this application

- Annotation-driven bean configuration, component scan.
- Using `PropertySource` to add more configuration values.
- Configuration injection
- `PostConstruct` - lifecycle method that can be used to validate the bean after construction
- `Lifecycle` implementation (the beans starts and stops together with Spring Framework)
- Custom `ApplicationEvent`s, publish and handle events
- `BeanPostProcessor`


# Application Structure

| Class | Explanation                                                  |
| ---- |--------------------------------------------------------------|
| `SpringApplicationStarter` | Starts the spring application                                |
| `Application` | The application main class.                                  |
| `BuildingEventReader` | Reads events from the input json file.                       |
| `BuildingEventHandler` | Handles `BuildingEvents` and creates alram objects, if needed. |
| `AlarmWriter` | Writes the alarms into a text file                           |
| `BeanListerBeanPostProcessor` | BeanPostProcessor that lists all beans of the spring context.

Note: `BuildingEventReader`, which is the source of events, is loosely coupled with `BuildingEventHandler`, which handles the events, via Spring ApplicationEvents.

Implement the classes according to the following:


## Application
- Inject `BuildingEventReader`
- In run() method call buildingEventReader.readEvents() to start event processing.

## BuildingEventReader
- setEventFilePath(): Spring should call this setter automatically, and inject the configuration value at ${event.file} key.
- checkInputFile(): spring should call it automatically (use PostConstruct annotation)
- implement `ApplicationEventPublisherAware` interface, so that Spring will set `ApplicationEventPublisher` object.
- readEvents(): replace **// TODO: publish the events here** line, with the actual publishing of `BuildingEvent`.

## AlarmWriter
- Create the class.
- Inject ${alarm.file}, to configure the alarm file path.
- Implement `Lifecycle` interface.
  - start() method: open the alarm file.
  - stop() method: close the alarm file.
  - isRunning() method: provide meaningful implementation

Note: this class is modelling an open resource, which is usually a network connection to a database or message broker.
The class uses opens and closes the resource at the same time as Spring framework starts and stops.

## BuildingEventHandler
- Create the class. It should implement `ApplicationListener<BuildingEvent>` interface.
- inject `AlarmWriter`
- onApplicationEvent(BuildingEvent): if the event type is `FIRE_DETECTION` or `TEMPERATURE_TOO_HIGH`, then create an alarm and send to AlarmWriter

## BeanListerBeanPostProcessor
- Create the class. Implement BeanPostProcessor.
- postProcessBeforeInitialization(): LOG the name of the created beans.

# Playing with the Application

Run the application, and analyse the log output to understand how the actions happen one after the other.

You should see something like this:

    [main] INFO com.epam.training.spring.SpringApplicationStarter - Creating ApplicationContext...
    [main] INFO com.epam.training.spring.beans.BeanListerBeanPostProcessor - Bean initialization: springConfig
    [main] INFO com.epam.training.spring.beans.BeanListerBeanPostProcessor - Bean initialization: alarmWriter
    [main] INFO com.epam.training.spring.beans.BeanListerBeanPostProcessor - Bean initialization: buildingEventReader
    [main] INFO com.epam.training.spring.beans.BuildingEventReader - EventReader.checkInputFile() has been called
    [main] INFO com.epam.training.spring.beans.BeanListerBeanPostProcessor - Bean initialization: application
    [main] INFO com.epam.training.spring.beans.BeanListerBeanPostProcessor - Bean initialization: buildingEventHandler
    [main] INFO com.epam.training.spring.SpringApplicationStarter - Starting ApplicationContext...
    [main] INFO com.epam.training.spring.beans.AlarmWriter - AlarmWriter has been started
    [main] INFO com.epam.training.spring.SpringApplicationStarter - Running application logic...
    [main] INFO com.epam.training.spring.beans.Application - run() method has been called
    [main] INFO com.epam.training.spring.beans.BuildingEventReader - Reading and publishing events...
    [main] INFO com.epam.training.spring.beans.AlarmWriter - AlarmWriter has been stopped

## Graceful shutdown

Change Application.run() method

- Add System.exit(0) call as a last instruction to the run() method
- See the difference in the log: AlarmWriter.stop() method does not get called.
- Change `SpringApplicationStarter`: right after applicationContext creation, call registerShutdownHook() on the context

# Other Technologies

Some other technologies used in this application

- SLF4J (Simple Logging Facade for Java) for writing log messages.
- Jackson ObjectMapper for simple json file processing.
