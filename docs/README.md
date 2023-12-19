# Introduction

The goal of this exercise is to practice some Spring Core framework features
that are not very often used in an average spring application,
but useful for a java developer to understand and practice.


# Problem Domain

Buildings are equipped with sensors that send events to a computer system.
Operators run an application that evaluates events, and in case of some danger or risk,
it automatically mitigates the problem by sending alerts to humans who can react and solve the problem.

Ideally such application should process streaming events,
but for simplicity this simple application reads a json input file, reads the events form that.

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

After processing the above event, the system will send an alarm with the following values:
("Call Mechanic", "Guest Apartment", "Room is hot")

Note: the alert file is a simple text file, each alert is printed in a separate line, format is not relevant.


# Spring Technologies in this application

- Annotation-driven bean configuration with component scanning.
- Using `PropertySource` to add more configuration values.
- Configuration value injection (`@Value`)
- Lifecycle methods (`@PostConstruct`) that can be used to validate the bean after construction.
- `Lifecycle` implementation: the beans starts and stops together with Spring Framework.
- Custom `ApplicationEvent`s, publish and handle events.
- `BeanPostProcessor` to modify beans
- Application Context `registerShutdownHook()` for graceful shutdown


# Application Structure

| Class | Explanation                                                   |
| ---- |---------------------------------------------------------------|
| `SpringApplicationStarter` | Starts the spring application                                 |
| `Application` | The application main class.                                   |
| `BuildingEventReader` | Reads events from the input json file.                        |
| `BuildingEventHandler` | Handles `BuildingEvents` and creates alarm objects.           |
| `FileWriterAlarmHandler` | Handles alarms and writes into a text file.                   |
| `BeanListerBeanPostProcessor` | BeanPostProcessor that lists all beans of the spring context. |

Note: `BuildingEventReader`, which is the source of events,
is loosely coupled with `BuildingEventHandler`, which handles the events, via Spring ApplicationEvents.

## Diagram

![](https://raw.githubusercontent.com/epam-java-cre/exercise-specification-images/main/spring-core-building-alarm/building-alarm.png)

Implement the classes according to the following:

## Application
- Inject `BuildingEventReader`
- In run() method call buildingEventReader.readEvents() to start event reading and processing.

## BuildingEventReader
- setEventFilePath(): Spring should call this setter automatically, and inject the configuration value at ${event.file.path} key.
- checkInputFile(): spring should call it automatically (use PostConstruct annotation)
- implement `ApplicationEventPublisherAware` interface, so that Spring will set `ApplicationEventPublisher` object.
- readEvents(): replace **// TODO: publish the events here** line, with the actual publishing of `BuildingEvent`.

## FileWriterAlarmHandler
- Create the class, let it be a Spring bean.
- Inject ${alarm.file.path}, to configure the alarm file path.
- Implement `Lifecycle` interface.
  - start() method: open the alarm file.
  - stop() method: close the alarm file.
  - isRunning() method: add meaningful implementation

Note: this class is modelling an open resource, which is usually a network connection to a database or message broker.
The class uses opens and closes the resource at the same time as Spring framework starts and stops.

## BuildingEventHandler
- Create the class. Let it be a Spring bean.
- It should implement `ApplicationListener<BuildingEvent>` interface.
- inject `AlarmHandler`
- onApplicationEvent(BuildingEvent): if the event type is `FIRE_DETECTION` or `TEMPERATURE_TOO_HIGH`, then create an alarm and send to AlarmHandler


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
- Change `SpringApplicationStarter`: right after applicationContext creation, call registerShutdownHook() on the context.

## BeanListerBeanPostProcessor
- Create the class. Implement BeanPostProcessor.
- postProcessBeforeInitialization(): LOG the name of the created beans.


# Other Technologies

Some other technologies used in this application

- SLF4J (Simple Logging Facade for Java) for writing log messages.
- Jackson ObjectMapper for simple json file processing.
