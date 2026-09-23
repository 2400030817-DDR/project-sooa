package com.careerlink.job.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.careerlink.job.entity.Job;
import com.careerlink.job.service.JobService;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {

        return jobService.createJob(job);
    }

    @GetMapping
    public List<Job> getAllJobs() {

        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(
            @PathVariable Long id) {

        Job job = jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(job);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public List<Job> getJobsByRecruiter(
            @PathVariable Long recruiterId) {

        return jobService.getJobsByRecruiter(recruiterId);
    }

    @GetMapping("/search/location")
    public List<Job> searchByLocation(
            @RequestParam String location) {

        return jobService.searchByLocation(location);
    }

    @GetMapping("/search/title")
    public List<Job> searchByTitle(
            @RequestParam String title) {

        return jobService.searchByTitle(title);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id,
            @RequestBody Job updatedJob) {

        Job job = jobService.updateJob(id, updatedJob);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {

        boolean deleted = jobService.deleteJob(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}