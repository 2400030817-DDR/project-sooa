package com.careerlink.application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.careerlink.application.entity.ApplicationStatus;
import com.careerlink.application.entity.JobApplication;
import com.careerlink.application.service.JobApplicationService;

@RestController
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public JobApplication apply(
            @RequestBody JobApplication application) {

        return service.apply(application);
    }

    @GetMapping
    public List<JobApplication> getAllApplications() {

        return service.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getApplicationById(
            @PathVariable Long id) {

        JobApplication application =
                service.getApplicationById(id);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }

    @GetMapping("/candidate/{candidateId}")
    public List<JobApplication> getByCandidate(
            @PathVariable Long candidateId) {

        return service.getApplicationsByCandidate(candidateId);
    }

    @GetMapping("/job/{jobId}")
    public List<JobApplication> getByJob(
            @PathVariable Long jobId) {

        return service.getApplicationsByJob(jobId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<JobApplication> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusRequest request) {

        JobApplication application =
                service.updateStatus(id, request.getStatus());

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long id) {

        JobApplication application =
                service.getApplicationById(id);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        service.deleteApplication(id);

        return ResponseEntity.noContent().build();
    }

    public static class StatusRequest {

        private ApplicationStatus status;

        public ApplicationStatus getStatus() {
            return status;
        }

        public void setStatus(ApplicationStatus status) {
            this.status = status;
        }
    }
}