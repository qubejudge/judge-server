package com.example.sender.services;


import com.example.sender.controller.SocketClient;
import com.example.sender.dto.ListSubmissions;
import com.example.sender.dto.SubmissionDataResponse;
import com.example.sender.dto.SubmissionMessage;
import com.example.sender.dto.SubmissionResponse;
import com.example.sender.dto.WorkerResponse;
import com.example.sender.entity.Submission;
import com.example.sender.entity.SubmissionData;
import com.example.sender.repository.SubmissionDataRepository;
import com.example.sender.repository.SubmissionRespository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpsRedirectSpec;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

    @Autowired
    private DirectExchange directExchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AsyncRabbitTemplate asyncRabbitTemplate;

    @Autowired
    SimpMessagingTemplate template;

    private SocketClient socketClient;

    private final SubmissionRespository submissionRespository;
    private final SubmissionDataRepository submissionDataRepository;

    public ResponseEntity<SubmissionResponse> submitFile(MultipartFile file, String input, String lang) throws IOException {
//        File convertFile = new File("./tmp/" + file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        String[] splits = fileName.split("\\.", 2);

//        String fileType = fileName.split(".");
        String fileType = splits[1];
        byte[] fileContent = file.getBytes();

        
        var submission = Submission.builder()
                .submissionStatus("PENDING")
                .userId("USER1")
                .filename(fileName)
                .filetype(fileType)
                .lang(fileType)
                .build();
        try {
            submissionRespository.save(submission);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
        }
        
        var submissionData = SubmissionData.builder()
                                           .submissionId(submission.getSubmissionId())
                                           .filedata(fileContent)
                                           .lang(fileType)
                                           .build();

        try {
            submissionDataRepository.save(submissionData);
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
        }

        var submissionMessage = SubmissionMessage.builder()
                        .id(submission.getSubmissionId())
                        .file(fileContent)
                        .fileType(fileType)
                        .input(input)
                        .build();

        Gson gson = new Gson();
        String json = gson.toJson(submissionMessage);
        
        RabbitConverterFuture<String> rabbitConverterFuture =
                asyncRabbitTemplate.convertSendAndReceive(
                        directExchange.getName(),
                        "rpc",
                        json);
        log.info("Sent Submission Task {} to Worker", submission.getSubmissionId());
        rabbitConverterFuture.whenComplete((result, ex) -> {
            if(ex == null){
                WorkerResponse workerResponse = gson.fromJson(result, WorkerResponse.class);
                log.info("Worker Response for submission task {} recieved as {}", submission.getSubmissionId(), workerResponse);
                
                // Store worker response to DB
                submission.setOutput(workerResponse.getOut());
                submission.setErrorCode(workerResponse.getErrorCode());
                submission.setMemoryUse(workerResponse.getMemory());
                submission.setTimeTaken(workerResponse.getTime());
                submission.setSubmissionStatus("COMPLETED");
                try {
                    submissionRespository.save(submission);
                    log.info("Updated submission results in DB for submissionID {}", submission.getSubmissionId());
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error(e.getMessage(), e);
                }

                successOperation(workerResponse, submission.getSubmissionId());
            }else{
                log.error("Worker could not complete submission task {}", submission.getSubmissionId());
            }
        });

        return new ResponseEntity<>(SubmissionResponse.builder().id(submission.getSubmissionId()).message("Submitted Successfully").build(), HttpStatusCode.valueOf(200));
    }

    public void successOperation(WorkerResponse workerResponse, String submissionId)
    {
        log.info("Sending worker response across {}", "/topic/message/"+submissionId);
        try {
            template.convertAndSend("/topic/message/" + submissionId, workerResponse);
            log.info("Sent worker response to {}", "/topic/message/"+submissionId);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Unable to send worker response to {}",  "/topic/message/"+submissionId);
        }
    }

    public ResponseEntity<ListSubmissions> getSubmissionsByUserID(String userID){
        try {
            var submissions = submissionRespository.findAllByUserId(userID);
            return new ResponseEntity<ListSubmissions>(ListSubmissions.builder().submissions(submissions).build(), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<ListSubmissions>(HttpStatusCode.valueOf(500));
    }

    public ResponseEntity<SubmissionDataResponse> getSubmissionDatabySubmissionID(String submissionID){
        try {
            var submissionData = submissionDataRepository.findById(submissionID);
            if(submissionData.isPresent()){
                // bytes to String
                String submissionCode = new String(submissionData.get().getFiledata(), StandardCharsets.UTF_8);
                return new ResponseEntity<SubmissionDataResponse>(SubmissionDataResponse.builder()
                                                                                        .submissionData(submissionCode)
                                                                                        .submissionID(submissionID)
                                                                                        .submissionLang(submissionData.get().getLang()).build() , HttpStatusCode.valueOf(200));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return new ResponseEntity<SubmissionDataResponse>(HttpStatusCode.valueOf(500));
    }

}
