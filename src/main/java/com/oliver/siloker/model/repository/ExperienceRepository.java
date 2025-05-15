package com.oliver.siloker.model.repository;

import com.oliver.siloker.model.entity.user.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

}
