package com.example.sender.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "submissions_data")
public class SubmissionData {
    @Id
    private String submissionId;
    @Lob
    @Column(length = 100000)
    private byte[] filedata;
    private String lang;
}
