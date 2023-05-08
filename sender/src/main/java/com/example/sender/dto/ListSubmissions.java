package com.example.sender.dto;

import java.util.List;

import com.example.sender.entity.Submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListSubmissions {
    private List<Submission> submissions;
}
