package com.bestarchitect.messaging.service;

import java.util.Map;


public interface MessageQueueConfigurerService {

    /**
     * @param queueName
     * @return if queue exists
     */
    boolean isQueueExists(String queueName);

    /**
     * @param exchangeName
     */
    void createExchange(String exchangeName);

    /**
     * @param queueName
     */
    void createQueue(String queueName);

    /**
     * @param queueName
     * @param args
     */
    void createQueue(String queueName, Map<String, Object> args);

    /**
     * @param queueName
     * @param routingKey
     * @param exchangeName
     */
    void createBinding(String queueName, String routingKey, String exchangeName);

}
