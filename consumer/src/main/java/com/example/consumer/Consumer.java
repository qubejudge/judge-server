package com.example.consumer;

import com.example.consumer.dto.SubmissionMessage;
import com.example.consumer.dto.WorkerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class Consumer {


    @RabbitListener(queues = "${queue.name}")
    public String receive(String message) throws InterruptedException, JSONException, IOException {
        //System.out.println("Message received from kedaQ : " + message);
        
        Gson gson = new Gson();
        SubmissionMessage obj = gson.fromJson(message, SubmissionMessage.class);
        log.info("Worker has received Submission Task {} from the judge-master", obj.getId());

        log.info("Worker is processing submission {} in lang {}", obj.getId(), obj.getFileType());
        File convertFile = new File("solution." + obj.getFileType());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(obj.getFile());
        fout.close();

        // Write input to testcase.txt
        File testcaseFileObj = new File("testcase.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(testcaseFileObj));
        writer.append(obj.getInput());
        writer.close();

        log.info("Worker is compiling and executing, submission {}", obj.getId());
        int exitCode = executeCode(obj.getId(), obj.getFileType());
        String output = "";
        String errorCode = "";
        Double time = 0.0;
        int mem = 0;
        if(exitCode == 0){
            log.info("Compilation and execution completed for submission {}", obj.getId());
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
                log.info("No error file created for submission {}", obj.getId());
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
                        try {
                            time = Double.parseDouble(splited[0]);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        
                        try {
                            mem = Integer.parseInt(splited[1]);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
                timeMem_reader.close();
            } catch (IOException e) {
                log.error("Unable to process time and memory output for submission {}", obj.getId());
            }
        }else{
            log.error("Unable to compile and execute submission {}", obj.getId());
            return "Unable to run code!";
        }
        WorkerResponse workerResponse = WorkerResponse.builder().out(output).memory(mem).time(time).errorCode(errorCode).build();
        String responseJson = gson.toJson(workerResponse);
        cleanup(obj.getFileType());
        log.info("Clean up for submission {} completed, sending callback to judge-master", obj.getId());
        return responseJson;
    }

    private void cleanup(String sol_ext) throws IOException {
        Files.deleteIfExists(Path.of("solution." + sol_ext));
        Files.deleteIfExists(Path.of("solution"));
        Files.deleteIfExists(Path.of("out.txt"));
        Files.deleteIfExists(Path.of("timeMem.txt"));
        Files.deleteIfExists(Path.of("errors.txt"));
    }


    //['./app/run.sh', 'cpp', 'out.txt', 'timeMem.txt', '10', '256']
    private int executeCode(String id, String fileType) throws InterruptedException {
        ProcessBuilder processBuilder
                = new ProcessBuilder();
        Thread.sleep(10000);
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
            StopWatch watch = new StopWatch();
            Process process = processBuilder.start();
            watch.start();

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            int exitCode = process.waitFor();
            watch.stop();
            log.info("Time taken by submission {} is {}", id, watch.getTotalTimeMillis());
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
