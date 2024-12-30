package com.thecoffe.ms_the_coffee.repositories;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.thecoffe.ms_the_coffee.models.PasswordReset;

public interface PasswordResetRepository extends CrudRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordReset p WHERE p.expirationTime < :currentTime")
    void deleteExpiredTokens(@Param("currentTime") Instant currentTime);

}
