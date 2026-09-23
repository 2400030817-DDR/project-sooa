package com.application_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import com.careerlink.application.entity.ApplicationStatus;
import com.careerlink.application.entity.JobApplication;
import com.careerlink.application.repository.JobApplicationRepository;
import com.careerlink.application.service.JobApplicationService;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceApplicationTests {

    @Mock
    private JobApplicationRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    @Test
    void testGetAllApplications() {

        JobApplication app1 = new JobApplication();
        JobApplication app2 = new JobApplication();

        when(repository.findAll())
                .thenReturn(List.of(app1, app2));

        List<JobApplication> result =
                jobApplicationService.getAllApplications();

        assertEquals(2, result.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetApplicationById() {

        JobApplication application = new JobApplication();

        when(repository.findById(1L))
                .thenReturn(java.util.Optional.of(application));

        JobApplication result =
                jobApplicationService.getApplicationById(1L);

        assertEquals(application, result);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetApplicationsByCandidate() {

        JobApplication app1 = new JobApplication();
        JobApplication app2 = new JobApplication();

        when(repository.findByCandidateId(2L))
                .thenReturn(List.of(app1, app2));

        List<JobApplication> result =
                jobApplicationService.getApplicationsByCandidate(2L);

        assertEquals(2, result.size());

        verify(repository, times(1))
                .findByCandidateId(2L);
    }

    @Test
    void testGetApplicationsByJob() {

        JobApplication application = new JobApplication();

        when(repository.findByJobId(5L))
                .thenReturn(List.of(application));

        List<JobApplication> result =
                jobApplicationService.getApplicationsByJob(5L);

        assertEquals(1, result.size());

        verify(repository, times(1))
                .findByJobId(5L);
    }

    @Test
    void testUpdateStatus() {

        JobApplication application = new JobApplication();

        when(repository.findById(7L))
                .thenReturn(java.util.Optional.of(application));

        when(repository.save(application))
                .thenReturn(application);

        JobApplication result =
                jobApplicationService.updateStatus(
                        7L,
                        ApplicationStatus.UNDER_REVIEW);

        assertEquals(
                ApplicationStatus.UNDER_REVIEW,
                result.getStatus());

        verify(repository, times(1))
                .findById(7L);

        verify(repository, times(1))
                .save(application);
    }
}