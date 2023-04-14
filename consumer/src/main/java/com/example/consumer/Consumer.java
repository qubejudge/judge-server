package com.example.consumer;

import com.example.consumer.dto.SubmissionMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
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
    public String receive(String message) throws InterruptedException, JSONException, IOException {
//        System.out.println("Message received from kedaQ : " + message);
        Gson gson = new Gson();
        SubmissionMessage obj = gson.fromJson(message, SubmissionMessage.class);

        File convertFile = new File("solution." + obj.getFileType());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(obj.getFile());
        fout.close();

        executeCode(obj.getId(), obj.getFileType());

        Files.deleteIfExists(Path.of("solution." + obj.getFileType()));
        Files.deleteIfExists(Path.of("solution"));
        Files.deleteIfExists(Path.of("out.txt"));
        Files.deleteIfExists(Path.of("timeMem.txt"));

        return "code executed successfully";
    }


    //['./app/run.sh', 'cpp', 'out.txt', 'timeMem.txt', '10', '256']
    private void executeCode(String id, String fileType) throws InterruptedException {
        ProcessBuilder processBuilder
                = new ProcessBuilder();

        List<String> builderList = new ArrayList<String>();
        builderList.add("./run.sh");
        builderList.add(fileType);
        builderList.add("out.txt");
        builderList.add("timeMem.txt");
        builderList.add("10");
        builderList.add("256");

        try {

            processBuilder.command(builderList);
            Process process = processBuilder.start();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : "
                    + exitCode);


        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
