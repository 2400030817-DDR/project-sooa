package com.careerlink.job.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.careerlink.job.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiterId(Long recruiterId);

    List<Job> findByLocationContainingIgnoreCase(String location);

    List<Job> findByTitleContainingIgnoreCase(String title);
}