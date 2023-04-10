package com.example.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage")
    public void send(@RequestParam("msg") String msg){
        System.out.println("oihhhh");
        System.out.println("Send Message: " + msg);
        rabbitTemplate.convertAndSend("messageq", msg);
    }

}


