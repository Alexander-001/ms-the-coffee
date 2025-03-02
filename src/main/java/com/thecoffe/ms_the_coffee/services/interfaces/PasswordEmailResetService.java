package com.thecoffe.ms_the_coffee.services.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.thecoffe.ms_the_coffee.models.PasswordEmailReset;

public interface PasswordEmailResetService {

    List<PasswordEmailReset> findAll();

    Optional<PasswordEmailReset> findByToken(String token);

    PasswordEmailReset save(Long userId, String token, Instant expirationTime);

    Optional<PasswordEmailReset> update(Long id, PasswordEmailReset passwordReset);

    Optional<PasswordEmailReset> delete(Long id);

    void cleanExpiredTokens();

}
