package com.example.tracker_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de nuestros componentes en RabbitMQ
    public static final String QUEUE_NAME = "price.drop.queue";
    public static final String EXCHANGE_NAME = "price.drop.exchange";
    public static final String ROUTING_KEY = "price.drop.routing.key";

    // 1. Creamos la Cola (Buzón)
    @Bean
    public Queue priceDropQueue() {
        return new Queue(QUEUE_NAME, true); // true = la cola sobrevive si RabbitMQ se reinicia
    }

    // 2. Creamos el Exchange (Cartero)
    @Bean
    public TopicExchange priceDropExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 3. Unimos la Cola y el Exchange con el Routing Key (Dirección)
    @Bean
    public Binding binding(Queue priceDropQueue, TopicExchange priceDropExchange) {
        return BindingBuilder.bind(priceDropQueue).to(priceDropExchange).with(ROUTING_KEY);
    }

    // 4. Convertidor para enviar objetos Java como JSON automáticamente
    @Bean
    @SuppressWarnings("deprecation")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}