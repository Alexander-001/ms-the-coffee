package com.thecoffe.ms_the_coffee.services.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.thecoffe.ms_the_coffee.models.PasswordReset;

public interface PasswordResetService {

    List<PasswordReset> findAll();

    Optional<PasswordReset> findByToken(String token);

    PasswordReset save(Long userId, String token, Instant expirationTime);

    Optional<PasswordReset> update(Long id, PasswordReset passwordReset);

    Optional<PasswordReset> delete(Long id);

    void cleanExpiredTokens();

}
