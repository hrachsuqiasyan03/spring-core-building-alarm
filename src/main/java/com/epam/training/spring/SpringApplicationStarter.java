package com.epam.training.spring;

import com.epam.training.spring.beans.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringApplicationStarter {
    private static final Logger LOG = LoggerFactory.getLogger(SpringApplicationStarter.class);

    public static void main(String[] args) {
        LOG.info("Creating ApplicationContext...");
        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class)) {

            LOG.info("Starting ApplicationContext...");
            applicationContext.start();

            LOG.info("Running application logic...");
            Application application = applicationContext.getBean(Application.class);
            application.run();
        }
    }
}
