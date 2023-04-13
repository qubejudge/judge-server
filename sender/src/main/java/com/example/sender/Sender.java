package com.example.sender;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/sender")
public class Sender {

    @Autowired
	private DirectExchange directExchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AsyncRabbitTemplate asyncRabbitTemplate;

    @GetMapping("/sendMessage")
    public void send(@RequestParam("msg") String msg){
        //System.out.println("oihhhh");
        System.out.println("Send Message: " + msg);
        //String resp = (String) rabbitTemplate.convertSendAndReceive(directExchange.getName(), "rpc", msg);
        //System.out.println("Response from worker: " + resp);
    
        RabbitConverterFuture<String> rabbitConverterFuture =
           asyncRabbitTemplate.convertSendAndReceive(
               directExchange.getName(),
               "rpc",
               msg);
        
        rabbitConverterFuture.whenComplete((result, ex) -> {
            if(ex == null){
                System.out.println("Response from worker: " + result);
            }else{
                System.out.println("Worker could not perform operation");
            }
        });
    }
}


