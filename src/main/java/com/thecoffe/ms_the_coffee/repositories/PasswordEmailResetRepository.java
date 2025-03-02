package com.thecoffe.ms_the_coffee.repositories;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.thecoffe.ms_the_coffee.models.PasswordEmailReset;

public interface PasswordEmailResetRepository extends CrudRepository<PasswordEmailReset, Long> {

    Optional<PasswordEmailReset> findByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordEmailReset p WHERE p.expirationTime < :currentTime")
    void deleteExpiredTokens(@Param("currentTime") Instant currentTime);

}
