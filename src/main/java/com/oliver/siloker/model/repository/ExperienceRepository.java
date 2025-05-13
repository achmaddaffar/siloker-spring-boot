package com.oliver.siloker.model.repository;

import com.oliver.siloker.model.entity.user.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM Experience experience WHERE
            experience.userId = :userId
            """)
    void deleteExperiencesByUserId(Long userId);
}
