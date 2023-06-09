package com.example.sender.repository;

import com.example.sender.entity.Submission;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRespository extends JpaRepository<Submission, String> {
    List<Submission> findAllByUserId(String userId);
}
