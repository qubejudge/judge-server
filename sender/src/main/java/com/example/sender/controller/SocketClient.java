package com.example.sender.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class SocketClient {

    @SendTo("/topic/success")
    public String sendSuccess(String result)
    {
        System.out.println("oiiiiii");
        System.out.println(result);
        return "yayyyy";
    }
}
