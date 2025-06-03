package com.oliver.siloker.model.repository;

import com.oliver.siloker.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT user FROM User user WHERE
            user.employer.id = :employerId
            """)
    Optional<User> findByEmployerId(Long employerId);
}
