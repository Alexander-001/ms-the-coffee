package com.thecoffe.ms_the_coffee.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thecoffe.ms_the_coffee.models.PasswordReset;
import com.thecoffe.ms_the_coffee.repositories.PasswordResetRepository;
import com.thecoffe.ms_the_coffee.services.interfaces.PasswordResetService;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Override
    public List<PasswordReset> findAll() {
        return (List<PasswordReset>) passwordResetRepository.findAll();
    }

    @Override
    public Optional<PasswordReset> findByToken(String token) {
        return passwordResetRepository.findByToken(token);
    }

    @Override
    public PasswordReset save(Long userId, String token, Instant expirationTime) {
        PasswordReset passwordResetToken = new PasswordReset();
        passwordResetToken.setUserId(userId);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpirationTime(expirationTime);
        return passwordResetRepository.save(passwordResetToken);
    }

    @Override
    public Optional<PasswordReset> update(Long id, PasswordReset passwordReset) {
        Optional<PasswordReset> passwordResetDb = passwordResetRepository.findById(id);
        if (passwordResetDb.isPresent()) {
            PasswordReset updatePasswordReset = passwordResetDb.get();
            return Optional.of(passwordResetRepository.save(updatePasswordReset));
        }
        return passwordResetDb;
    }

    @Override
    public Optional<PasswordReset> delete(Long id) {
        Optional<PasswordReset> passwordReset = passwordResetRepository.findById(id);
        passwordReset.ifPresent(password -> {
            passwordResetRepository.delete(password);
        });
        return passwordReset;
    }

    // * Run every 1 hour
    @Override
    @Scheduled(fixedRate = 3600000) // * (60 * 60 * 1000)
    public void cleanExpiredTokens() {
        Instant now = Instant.now();
        passwordResetRepository.deleteExpiredTokens(now);
    }

}
