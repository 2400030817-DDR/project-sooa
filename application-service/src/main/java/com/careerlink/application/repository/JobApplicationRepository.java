package com.careerlink.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careerlink.application.entity.JobApplication;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByCandidateId(Long candidateId);

    List<JobApplication> findByJobId(Long jobId);
}