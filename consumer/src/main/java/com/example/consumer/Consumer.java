package com.example.consumer;

import com.example.consumer.dto.SubmissionMessage;
import com.example.consumer.dto.WorkerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.json.JSONException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        int exitCode = executeCode(obj.getId(), obj.getFileType());
        String output = "";
        String errorCode = "";
        Double time = 0.0;
        int mem = 0;
        if(exitCode == 0){
            output = Files.readString(Paths.get("out.txt"));
            //return output;
            BufferedReader err_reader, timeMem_reader;
            try {
                err_reader = new BufferedReader(new FileReader("errors.txt"));
                String line = err_reader.readLine();
                if(line != null){
                    errorCode = line;
                }
                err_reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                timeMem_reader = new BufferedReader(new FileReader("timeMem.txt"));
                String line = timeMem_reader.readLine();
                if(line != null){
                    String[] splited = line.split("\\s+");
                    if(splited[0].equals("Command")){
                        line = timeMem_reader.readLine();
                    }
                    splited = line.split("\\s+");
                    if(line != null){
                        time = Double.parseDouble(splited[0]);
                        mem = Integer.parseInt(splited[1]);
                    }
                }
                timeMem_reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            return "Unable to run code!";
        }
        WorkerResponse workerResponse = WorkerResponse.builder().out(output).memory(mem).time(time).errorCode(errorCode).build();
        String responseJson = gson.toJson(workerResponse);
        cleanup(obj.getFileType());
        return responseJson;
    }

    private void cleanup(String sol_ext) throws IOException {
        Files.deleteIfExists(Path.of("solution." + sol_ext));
        Files.deleteIfExists(Path.of("solution"));
        //Files.deleteIfExists(Path.of("out.txt"));
        Files.deleteIfExists(Path.of("timeMem.txt"));
        Files.deleteIfExists(Path.of("errors.txt"));
    }


    //['./app/run.sh', 'cpp', 'out.txt', 'timeMem.txt', '10', '256']
    private int executeCode(String id, String fileType) throws InterruptedException {
        ProcessBuilder processBuilder
                = new ProcessBuilder();

        List<String> builderList = new ArrayList<String>();
        builderList.add("./run.sh");
        builderList.add(fileType);
        builderList.add("out.txt");
        builderList.add("timeMem.txt");
        builderList.add("10");
        builderList.add("256");
        builderList.add("errors.txt");

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
            return exitCode;

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
