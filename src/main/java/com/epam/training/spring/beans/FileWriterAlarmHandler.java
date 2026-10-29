package com.epam.training.spring.beans;

import com.epam.training.spring.model.Alarm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.context.Lifecycle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class FileWriterAlarmHandler implements Lifecycle , AlarmHandler  {

    @Value("${alarm.file.path}")
    private String alarmFilePath;

    private FileWriter fileWriter = null;
    private boolean running = false;

    @Override
    public void start() {
        try {
            File file = new File(alarmFilePath);
            File parent = file.getParentFile();
            if (parent != null ) {
                Files.createDirectories(parent.toPath());
            }
            fileWriter= new FileWriter (alarmFilePath);
            running = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        @Override
    public void stop() {
        try{
            if (fileWriter != null) {
                fileWriter.close();
            }
            running = false;
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void handleAlarm(Alarm alarm) {
        if (fileWriter == null) {
            throw new IllegalStateException ("FileWriter is null");
        }
        String line = String.format ("%s | %s | %s",
                alarm.action (), alarm.location (), alarm.reason ()
                        + System.lineSeparator ());
        try {
            fileWriter.write (line);
            fileWriter.flush ();
        }catch (IOException e) {
            throw new RuntimeException (e);
        }
    }

    @Value ("${alarm.file.path}")
    public void setAlarmFilePath(String alarmFilePath) {
        this.alarmFilePath = alarmFilePath;
    }
}
