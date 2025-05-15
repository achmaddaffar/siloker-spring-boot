package com.oliver.siloker.model.repository;

import com.oliver.siloker.model.entity.user.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

}
