package com.example.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SenderApplication.class, args);
	}

	// @Bean
	// public Queue queue() {
	// 	return new Queue("messageq");
	// }


}
