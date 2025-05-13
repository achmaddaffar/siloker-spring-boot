package com.oliver.siloker.model.repository;

import com.oliver.siloker.model.entity.user.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM Skill skill WHERE
            skill.userId = :userId
            """)
    void deleteSkillsByUserId(Long userId);
}
