package com.careerlink.job.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.careerlink.job.entity.Job;
import com.careerlink.job.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(Job job) {

        job.setPostedAt(LocalDateTime.now());

        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {

        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {

        return jobRepository.findById(id).orElse(null);
    }

    public List<Job> getJobsByRecruiter(Long recruiterId) {

        return jobRepository.findByRecruiterId(recruiterId);
    }

    public List<Job> searchByLocation(String location) {

        return jobRepository
                .findByLocationContainingIgnoreCase(location);
    }

    public List<Job> searchByTitle(String title) {

        return jobRepository
                .findByTitleContainingIgnoreCase(title);
    }

    public Job updateJob(Long id, Job updatedJob) {

        Job job = jobRepository.findById(id).orElse(null);

        if (job == null) {
            return null;
        }

        job.setRecruiterId(updatedJob.getRecruiterId());
        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setLocation(updatedJob.getLocation());
        job.setSalary(updatedJob.getSalary());
        job.setJobType(updatedJob.getJobType());
        job.setSkills(updatedJob.getSkills());

        return jobRepository.save(job);
    }

    public boolean deleteJob(Long id) {

        if (!jobRepository.existsById(id)) {
            return false;
        }

        jobRepository.deleteById(id);

        return true;
    }
}