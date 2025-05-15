package com.oliver.siloker.model.repository;

import com.oliver.siloker.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("""
            SELECT job FROM Job job WHERE
            LOWER(job.title) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(job.description) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    Page<Job> filter(String query, Pageable pageable);
}
