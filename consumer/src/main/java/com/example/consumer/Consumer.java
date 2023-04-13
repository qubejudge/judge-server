package com.example.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


@Component
public class Consumer {

//    @RabbitListener(queues = "messageq")
//    public void listen(String message) {
//        System.out.println("Message received from kedaQ : " + message);
//    }

    // @Value("${queue.name}")
    // private String queueName;

    @RabbitListener(queues = "${queue.name}")
    public String receive(String message) throws InterruptedException {
        System.out.println("Message received from kedaQ : " + message);
        Thread.sleep(5000);
//        StopWatch watch = new StopWatch();
//        watch.start();
//        System.out.println("instance  [x] Received '" + in + "'");
//        doWork(in);
//        watch.stop();
//        System.out.println("instance  [x] Done in " + watch.getTotalTimeSeconds() + "s");
        return "Hello from the judge worker";
        
    }


    private void doWork(String in) throws InterruptedException {
        Thread.sleep(10000);
        ProcessBuilder processBuilder
                = new ProcessBuilder();
//		in = "ls";
        List<String> builderList = List.of(in.split(" "));
        System.out.println(builderList);
        // add the list of commands to a list
//        try {
            // Using the list , trigger the command
//            processBuilder.command(builderList);
//            Process process = processBuilder.start();
//
//            // To read the output list
//            BufferedReader reader
//                    = new BufferedReader(new InputStreamReader(
//                    process.getInputStream()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("\nExited with error code : "
//                    + exitCode);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

}
