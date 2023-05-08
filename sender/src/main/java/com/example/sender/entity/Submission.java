package com.example.sender.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String submissionId;
    private String submissionStatus;
    private String userId;
    private String filename;
    private String filetype;
    private String lang;
    private String errorCode;
    private Double timeTaken;
    private Integer memoryUse;
    private String output;
}
