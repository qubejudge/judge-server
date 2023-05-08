package com.example.sender.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionDataResponse {
    private String submissionID;
    private String submissionData;
    private String submissionLang;
}
