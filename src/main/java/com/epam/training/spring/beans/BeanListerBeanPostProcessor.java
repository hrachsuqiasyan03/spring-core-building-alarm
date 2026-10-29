package com.epam.training.spring.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanListerBeanPostProcessor  implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanListerBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        LOGGER.info("Bean initialization: {}", beanName);
        return bean;
    }
}
