package com.example.sender.dto;

import jdk.jfr.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {
    private String id;
    private String message;
    private Boolean anonymous;
    private String anonymousId;
}
