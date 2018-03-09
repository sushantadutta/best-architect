package com.bestarchitect.messaging.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageQueueConfigurerServiceImpl implements MessageQueueConfigurerService {

    private static final Logger LOG = LogManager.getLogger(MessageQueueConfigurerServiceImpl.class);


    /**
     * @param queueName
     * @return if queue exists
     */
    @Override
    public boolean isQueueExists(String queueName) {
        return false;
    }

    /**
     * @param exchangeName
     */
    @Override
    public void createExchange(String exchangeName) {

    }

    /**
     * @param queueName
     */
    @Override
    public void createQueue(String queueName) {

    }

    /**
     * @param queueName
     * @param args
     */
    @Override
    public void createQueue(String queueName, Map<String, Object> args) {

    }

    /**
     * @param queueName
     * @param routingKey
     * @param exchangeName
     */
    @Override
    public void createBinding(String queueName, String routingKey, String exchangeName) {

    }
}
