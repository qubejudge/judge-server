package com.example.sender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sender.entity.SubmissionData;

public interface SubmissionDataRepository extends JpaRepository<SubmissionData, String> {
    
}
