package com.example.sender.controller;

import com.example.sender.dto.ListSubmissions;
import com.example.sender.dto.SubmissionDataResponse;
import com.example.sender.dto.SubmissionResponse;
import com.example.sender.entity.Submission;
import com.example.sender.services.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class SubmissionController {

    @Autowired
    private final SubmissionService submissionService;

    @GetMapping("/hi")
    public ResponseEntity<String> pingEndpoint()
    {
        return new ResponseEntity<String>("hiiii", HttpStatusCode.valueOf(200));
    }

    @PostMapping("/submit-file")
    public ResponseEntity<SubmissionResponse> submitFile(@RequestParam("file")MultipartFile file, @RequestParam String input, @RequestParam String lang) throws IOException {
        return submissionService.submitFile(file, input, lang);
    }


    @GetMapping("/submissions")
    public ResponseEntity<ListSubmissions> listSubmissions(){
        return submissionService.getSubmissionsByUserID();
    }

    @GetMapping("/submission-code")
    public ResponseEntity<SubmissionDataResponse> fetchSubmissionCode(@RequestParam String submissionID){
        return submissionService.getSubmissionDatabySubmissionID(submissionID);
    } 
}
