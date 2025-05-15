package com.oliver.siloker.model.repository;

import com.oliver.siloker.model.entity.job.Job;
import com.oliver.siloker.model.entity.job.JobApplication;
import com.oliver.siloker.model.entity.user.JobSeeker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobSeekerAndJob(JobSeeker jobSeeker, Job job);

    @Query("""
            SELECT jobApplication FROM JobApplication jobApplication WHERE
            jobApplication.jobSeeker.id = :jobSeekerId
            """)
    Page<JobApplication> filterByJobSeekerId(Long jobSeekerId, Pageable pageable);

    @Query("""
            SELECT jobApplication FROM JobApplication jobApplication WHERE
            jobApplication.job.id = :jobId
            """)
    Page<JobApplication> filterByJobId(Long jobId, Pageable pageable);
}
