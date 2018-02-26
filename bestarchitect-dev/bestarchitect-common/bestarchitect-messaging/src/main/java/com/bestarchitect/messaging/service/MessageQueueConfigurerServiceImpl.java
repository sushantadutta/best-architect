package com.bestarchitect.messaging.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageQueueConfigurerServiceImpl implements MessageQueueConfigurerService {

    private static final Logger LOG = LogManager.getLogger(MessageQueueConfigurerServiceImpl.class);

    private AmqpAdmin rabbitAdmin;

    @Autowired
    public MessageQueueConfigurerServiceImpl(AmqpAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @Override
    public boolean isQueueExists(String queueName) {
        return rabbitAdmin.getQueueProperties(queueName) != null;
    }

    @Override
    public void createExchange(String exchangeName) {
        LOG.info("Creating exchange {} ", exchangeName);
        DirectExchange exchange = new DirectExchange(exchangeName, true, false);
        rabbitAdmin.declareExchange(exchange);
    }

    @Override
    public void createBinding(String queueName, String routingKey, String exchangeName) {
        LOG.info("Creating binding with exchange {} and queue {} with routing key {}", exchangeName, queueName,
                routingKey);
        Queue queue = new Queue(queueName, true);
        DirectExchange exchange = new DirectExchange(exchangeName, true, false);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    @Override
    public void createQueue(String queueName) {
        LOG.info("Create queue {} ", queueName);
        createQueue(queueName, null);
    }

    @Override
    public void createQueue(String queueName, Map<String, Object> args) {
        if (!isQueueExists(queueName)) {
            Queue queue = new Queue(queueName, true, false, false, args);
            rabbitAdmin.declareQueue(queue);
        }
    }

}
