package com.example.sender.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {
    
	@Value("${queue.name}")
	private String queueName;

	@Value("${exchange.name}")
	private String exchangeName;

	@Bean
	public Queue queue() {
		return new Queue(queueName);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	public Binding binding(DirectExchange exchange, Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with("rpc");
	}

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate){
    return new AsyncRabbitTemplate(rabbitTemplate);
    }

}
