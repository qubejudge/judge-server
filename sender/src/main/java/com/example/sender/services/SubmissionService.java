package com.example.sender.services;


import com.example.sender.dto.SubmissionMessage;
import com.example.sender.dto.SubmissionResponse;
import com.example.sender.entity.Submission;
import com.example.sender.repository.SubmissionRespository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    @Autowired
    private DirectExchange directExchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AsyncRabbitTemplate asyncRabbitTemplate;

    private final SubmissionRespository submissionRespository;

    public ResponseEntity<SubmissionResponse> submitFile(MultipartFile file) throws IOException {
//        File convertFile = new File("./tmp/" + file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        String[] splits = fileName.split("\\.", 2);

//        String fileType = fileName.split(".");
        String fileType = splits[1];
        byte[] fileContent = file.getBytes();

        var submission = Submission.builder()
                .submission_status("PENDING")
                .build();

        submissionRespository.save(submission);

        var submissionMessage = SubmissionMessage.builder()
                        .id(submission.getSubmission_id())
                        .file(fileContent)
                        .fileType(fileType)
                        .build();

        Gson gson = new Gson();
        String json = gson.toJson(submissionMessage);

        RabbitConverterFuture<String> rabbitConverterFuture =
                asyncRabbitTemplate.convertSendAndReceive(
                        directExchange.getName(),
                        "rpc",
                        json);

        rabbitConverterFuture.whenComplete((result, ex) -> {
            if(ex == null){
                System.out.println("Response from worker: " + result);
            }else{
                System.out.println("Worker could not perform operation");
            }
        });

        return new ResponseEntity<>(SubmissionResponse.builder().id(submission.getSubmission_id()).build(), HttpStatusCode.valueOf(200));
    }
}
