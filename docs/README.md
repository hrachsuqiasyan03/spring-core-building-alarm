# 🏢 Spring Core – Building Alarm System

A Java application built with the Spring Core Framework that simulates a building monitoring system. It processes building sensor events, detects potentially dangerous situations, and automatically generates alarms to notify responsible personnel.

This project demonstrates essential Spring Core features, including Dependency Injection, Inversion of Control (IoC), Application Events, Bean Lifecycle Management, and BeanPostProcessor.

## 📌 Project Overview

Modern buildings use sensors to monitor environmental conditions and detect potential hazards, such as fires or unusually high temperatures.

The Building Alarm System simulates this process by reading sensor events from a JSON file, processing them through Spring-managed components, and generating alarms when specific conditions are detected.

The application uses Spring's event-driven architecture to maintain loose coupling between event producers and event handlers.

### Example Scenario

A temperature sensor detects an unusually high temperature in a guest apartment.

**Input Event:**

```json
{
  "eventType": "TEMPERATURE_TOO_HIGH",
  "time": "2023-12-15T12:30:00+01:00",
  "location": "Guest Apartment",
  "message": "28°C"
}
```

**Generated Alarm:**

```text
Action: Call Mechanic
Location: Guest Apartment
Reason: Room is hot
```

The application also supports fire detection events and generates corresponding alarms.

## ⚙️ Technologies & Tools

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| Spring Core | Dependency Injection and application configuration |
| Spring Context | ApplicationContext and event management |
| Jackson | JSON file processing |
| SLF4J | Application logging |
| Maven | Dependency and build management |
| Git | Version control |

## 🏗️ Application Architecture

The application follows an event-driven architecture in which event reading, event processing, and alarm generation are handled by separate Spring-managed components.

### Main Components

| Component | Responsibility |
|---|---|
| `SpringApplicationStarter` | Initializes the Spring ApplicationContext and manages application startup and shutdown |
| `Application` | Main application component that starts event processing |
| `BuildingEventReader` | Reads sensor events from a JSON input file and publishes Spring application events |
| `BuildingEventHandler` | Listens for building events and generates alarms |
| `FileWriterAlarmHandler` | Manages the alarm output file and writes generated alarms |
| `BeanListerBeanPostProcessor` | Logs Spring bean names before initialization |

### Event Processing Flow

```text
             Application Startup
                     |
                     v
             Spring ApplicationContext
                     |
                     v
              Application.run()
                     |
                     v
           BuildingEventReader
                     |
                     v
          Read Events from JSON
                     |
                     v
        ApplicationEventPublisher
                     |
                     v
             BuildingEvent
                     |
                     v
          BuildingEventHandler
                     |
              Event Evaluation
                     |
          +----------+----------+
          |                     |
          v                     v
    FIRE_DETECTION      TEMPERATURE_TOO_HIGH
          |                     |
          +----------+----------+
                     |
                     v
               Create Alarm
                     |
                     v
          FileWriterAlarmHandler
                     |
                     v
              Alarm Output File
```

## 🌱 Spring Core Features Demonstrated

### 1. Inversion of Control (IoC)

The application uses Spring's IoC Container to manage object creation, dependencies, and bean lifecycle.

Instead of manually creating and connecting all application components, Spring manages their dependencies.

### 2. Dependency Injection (DI)

Dependencies are injected into Spring-managed beans.

For example:
- `Application` receives `BuildingEventReader`.
- `BuildingEventHandler` receives `AlarmHandler`.
- `BuildingEventReader` receives `ApplicationEventPublisher` through `ApplicationEventPublisherAware`.

This reduces direct dependencies between components and improves maintainability.

### 3. Annotation-Based Configuration

The application uses annotation-driven configuration and component scanning to register Spring beans.

Relevant Spring annotations include:

- `@Configuration`
- `@ComponentScan`
- `@Component`
- `@Value`
- `@PostConstruct`

### 4. PropertySource and Value Injection

Application-specific configuration values are injected using Spring's property resolution mechanism.

Examples:

```java
@Value("${event.file.path}")
private String eventFilePath;

@Value("${alarm.file.path}")
private String alarmFilePath;
```

This allows file paths to be configured externally rather than hardcoded in application classes.

### 5. Bean Lifecycle Management

The project demonstrates different mechanisms for managing the lifecycle of Spring beans.

**@PostConstruct**

Used to execute validation logic after bean dependency injection and before the bean is fully initialized.

**Lifecycle Interface**

`FileWriterAlarmHandler` implements Spring's `Lifecycle` interface.

- `start()` – Opens the alarm output resource.
- `stop()` – Closes the resource.
- `isRunning()` – Indicates whether the resource is currently active.

This demonstrates how Spring can manage resources that require explicit startup and shutdown operations.

### 6. Spring Application Events

The application uses Spring's event publishing and listening mechanism to achieve loose coupling between components.

- `BuildingEventReader` publishes events using `ApplicationEventPublisher`.
- `BuildingEventHandler` implements `ApplicationListener<BuildingEvent>`.
- The event handler processes incoming events and generates alarms.

The event reader does not need to know which component handles the published events.

### 7. BeanPostProcessor

`BeanListerBeanPostProcessor` implements Spring's `BeanPostProcessor` interface.

It uses `postProcessBeforeInitialization()` to log the names of Spring-managed beans before their initialization callbacks.

This demonstrates how Spring allows developers to customize bean processing.

### 8. Graceful Shutdown

The application uses `ApplicationContext.registerShutdownHook()` to ensure that Spring-managed lifecycle components receive their shutdown callbacks when the JVM terminates normally.

This allows resources such as file writers to be closed properly.

## 📂 Configuration

The application uses external properties to configure input and output file locations.

Example configuration:

```properties
event.file.path=src/main/resources/events.json
alarm.file.path=src/main/resources/alarms.txt
```

The actual paths should match the project's resource configuration.

## 🚀 Running the Application

### Prerequisites

- Java Development Kit (JDK)
- Maven
- Git

### Clone the Repository

```bash
git clone <repository-url>
```

### Navigate to the Project

```bash
cd spring-core-building-alarm
```

### Build the Application

```bash
mvn clean install
```

### Run the Application

Run the `SpringApplicationStarter` main class from your IDE or use the project's configured Maven execution command.

## 📋 Expected Behavior

When the application starts, Spring initializes the ApplicationContext and its managed beans.

The application then:

1. Initializes Spring-managed components.
2. Opens the alarm output file.
3. Reads events from the JSON input file.
4. Publishes `BuildingEvent` objects.
5. Processes events through `BuildingEventHandler`.
6. Generates alarms for supported event types.
7. Writes alarms to the output file.
8. Closes the output resource during graceful shutdown.

## 🎯 Learning Objectives

This project provides practical experience with:

- Understanding Spring IoC Container and Dependency Injection.
- Configuring Spring beans using annotations.
- Injecting configuration values using `@Value`.
- Working with bean initialization callbacks using `@PostConstruct`.
- Managing resources through the Spring `Lifecycle` interface.
- Publishing and handling custom Spring Application Events.
- Understanding loose coupling and event-driven architecture.
- Extending bean initialization using `BeanPostProcessor`.
- Managing application shutdown using `registerShutdownHook()`.
- Processing JSON data with Jackson.

## 📚 Key Takeaways

The Building Alarm System demonstrates how Spring Core can manage application components, coordinate communication through application events, and control resource lifecycles.

Although the application uses JSON files instead of real-time sensor streams, its architecture illustrates important concepts that are also applicable to larger enterprise Java applications.

---

**Project:** Spring Core – Building Alarm System  
**Language:** Java  
**Framework:** Spring Core  
**Purpose:** Educational project focused on Spring Framework fundamentals.
