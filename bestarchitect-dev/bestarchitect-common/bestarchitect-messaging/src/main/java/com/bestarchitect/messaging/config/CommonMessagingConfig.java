package com.bestarchitect.messaging.config;

import com.bestarchitect.messaging.constant.MessagingConstants;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.messaging.RabbitConnectionFactoryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class CommonMessagingConfig extends AbstractCloudConfig {

    /**
     * @param env
     * @return Connection factory
     */
    @Bean
    public ConnectionFactory rabbitConnectionFactory(Environment env) {
        int channelCacheSize = Integer.parseInt(env.getProperty(MessagingConstants.CHANNEL_CACHE_SIZE));
        RabbitConnectionFactoryConfig config = new RabbitConnectionFactoryConfig(channelCacheSize);
        return connectionFactory().rabbitConnectionFactory(config);
    }

    /**
     * @param rabbitConnectionFactory
     * @return RabbitMQ admin
     */
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory rabbitConnectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * @param rabbitConnectionFactory
     * @param retryTemplate
     * @param env
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitConnectionFactory, RetryTemplate retryTemplate,
                                         Environment env) {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setExchange(env.getProperty(MessagingConstants.AUDIT_EXCHANGE_NAME));
        template.setRetryTemplate(retryTemplate);
        template.setRecoveryCallback(context -> {
            throw new AmqpException("Not able to send the message");
        });
        return template;
    }

    /**
     * @param env
     * @return Retry template
     */
    @Bean
    public RetryTemplate retryTemplate(Environment env) {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(Integer.parseInt(env.getProperty(MessagingConstants.RETRY_INITIAL_INTERVAL)));
        backOffPolicy.setMaxInterval(Integer.parseInt(env.getProperty(MessagingConstants.RETRY_MAX_INTERVAL)));
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(Integer.parseInt(env.getProperty(MessagingConstants.RETRY_MAX_ATTEMPTS)));
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    /**
     * @param env
     * @return return audit exchange to which message will be published
     */
    @Bean
    public DirectExchange auditExchange(Environment env) {
        return new DirectExchange(env.getProperty(MessagingConstants.AUDIT_EXCHANGE_NAME), true, false);
    }

    /**
     * @param env
     * @return Dead letter exchange
     */
    @Bean
    public DirectExchange auditDeadLetterExchange() {
        return new DirectExchange(MessagingConstants.DEAD_LETTER_EXCHANGE, true, false);
    }

    /**
     * @return Queue
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(MessagingConstants.DEAD_LETTER_QUEUE, true, false, false);
    }

    /**
     * @param auditDeadLetterExchange
     * @param deadLetterQueue
     * @return Dead letter queue binding
     */
    @Bean
    public Binding queueBinding(DirectExchange auditDeadLetterExchange, Queue deadLetterQueue) {
        return BindingBuilder.bind(deadLetterQueue).to(auditDeadLetterExchange)
                .with(MessagingConstants.DEAD_LETTER_ROUTING_KEY);
    }

}
