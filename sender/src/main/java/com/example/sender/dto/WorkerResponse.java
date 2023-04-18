package com.example.sender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerResponse {
    private String errorCode;
    private Double time;
    private Integer memory;
    private String out;
}
