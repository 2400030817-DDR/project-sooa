package com.job_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.careerlink.job.entity.Job;
import com.careerlink.job.repository.JobRepository;
import com.careerlink.job.service.JobService;

@ExtendWith(MockitoExtension.class)
class JobServiceApplicationTests {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void testGetAllJobs() {

        Job job1 = new Job();
        Job job2 = new Job();

        when(jobRepository.findAll())
                .thenReturn(List.of(job1, job2));

        List<Job> result = jobService.getAllJobs();

        assertEquals(2, result.size());

        verify(jobRepository, times(1)).findAll();
    }
    @Test
    void testGetJobById() {

        Job job = new Job();

        when(jobRepository.findById(1L))
                .thenReturn(java.util.Optional.of(job));

        Job result = jobService.getJobById(1L);

        assertEquals(job, result);

        verify(jobRepository, times(1))
                .findById(1L);
    }
    @Test
    void testGetJobsByRecruiter() {

        Job job1 = new Job();
        Job job2 = new Job();

        when(jobRepository.findByRecruiterId(5L))
                .thenReturn(List.of(job1, job2));

        List<Job> result = jobService.getJobsByRecruiter(5L);

        assertEquals(2, result.size());

        verify(jobRepository, times(1))
                .findByRecruiterId(5L);
    }
    @Test
    void testSearchByLocation() {

        Job job = new Job();

        when(jobRepository.findByLocationContainingIgnoreCase("Hyderabad"))
                .thenReturn(List.of(job));

        List<Job> result =
                jobService.searchByLocation("Hyderabad");

        assertEquals(1, result.size());

        verify(jobRepository, times(1))
                .findByLocationContainingIgnoreCase("Hyderabad");
    }
    @Test
    void testSearchByTitle() {

        Job job = new Job();

        when(jobRepository.findByTitleContainingIgnoreCase("Java"))
                .thenReturn(List.of(job));

        List<Job> result =
                jobService.searchByTitle("Java");

        assertEquals(1, result.size());

        verify(jobRepository, times(1))
                .findByTitleContainingIgnoreCase("Java");
    }
}
