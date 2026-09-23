package com.careerlink.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.careerlink.application.dto.JobResponse;
import com.careerlink.application.dto.ProfileResponse;
import com.careerlink.application.entity.ApplicationStatus;
import com.careerlink.application.entity.JobApplication;
import com.careerlink.application.repository.JobApplicationRepository;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    private final RestTemplate restTemplate;

    public JobApplicationService(
            JobApplicationRepository repository,
            RestTemplate restTemplate) {

        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public JobApplication apply(JobApplication application) {

        try {

            JobResponse job = restTemplate.getForObject(
                    "http://JOB-SERVICE/jobs/{id}",
                    JobResponse.class,
                    application.getJobId());

            if (job == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Job does not exist");
            }

        } catch (ResponseStatusException e) {

            throw e;

        } catch (Exception e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to verify job with Job Service");
        }

        try {

            ProfileResponse profile = restTemplate.getForObject(
                    "http://PROFILE-SERVICE/profiles/user/{userId}",
                    ProfileResponse.class,
                    application.getCandidateId());

            if (profile == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Candidate profile does not exist");
            }

        } catch (ResponseStatusException e) {

            throw e;

        } catch (Exception e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to verify candidate with Profile Service");
        }

        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        return repository.save(application);
    }

    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }

    public JobApplication getApplicationById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<JobApplication> getApplicationsByCandidate(
            Long candidateId) {

        return repository.findByCandidateId(candidateId);
    }

    public List<JobApplication> getApplicationsByJob(Long jobId) {
        return repository.findByJobId(jobId);
    }

    public JobApplication updateStatus(
            Long id,
            ApplicationStatus status) {

        JobApplication application =
                repository.findById(id).orElse(null);

        if (application == null) {
            return null;
        }

        application.setStatus(status);

        return repository.save(application);
    }

    public void deleteApplication(Long id) {
        repository.deleteById(id);
    }
}