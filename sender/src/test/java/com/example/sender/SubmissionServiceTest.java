package com.example.sender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.UUIDNamingStrategy;

import com.example.sender.entity.Submission;
import com.example.sender.entity.SubmissionData;
import com.example.sender.repository.SubmissionDataRepository;
import com.example.sender.repository.SubmissionRespository;
import com.example.sender.services.SubmissionService;

@ExtendWith(MockitoExtension.class)
public class SubmissionServiceTest {
    
    @Mock
    private SubmissionDataRepository submissionRepository;

    @InjectMocks
    private SubmissionService service;

    @Test
    void get_submission(){
        // Arrange
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        final var submissionToSave = SubmissionData.builder().submissionId(uuid).build();
        when(submissionRepository.findById(uuid)).thenReturn(Optional.of(submissionToSave));

        // // Act
        final var actual = service.getSubmissionDatabySubmissionID(uuid);

        // // Assert
        // /assertThat(actual.getBody()).usingRecursiveComparison().isEqualTo(submissionToSave);
        // verify(repository, times(1)).save(any(Student.class));
        // verifyNoMoreInteractions(repository);
    }

    
}
