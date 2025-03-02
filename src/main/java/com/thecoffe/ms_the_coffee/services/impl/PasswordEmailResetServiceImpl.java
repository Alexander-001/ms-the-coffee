package com.thecoffe.ms_the_coffee.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thecoffe.ms_the_coffee.models.PasswordEmailReset;
import com.thecoffe.ms_the_coffee.repositories.PasswordEmailResetRepository;
import com.thecoffe.ms_the_coffee.services.interfaces.PasswordEmailResetService;

@Service
public class PasswordEmailResetServiceImpl implements PasswordEmailResetService {

    @Autowired
    private PasswordEmailResetRepository passwordResetRepository;

    @Override
    public List<PasswordEmailReset> findAll() {
        return (List<PasswordEmailReset>) passwordResetRepository.findAll();
    }

    @Override
    public Optional<PasswordEmailReset> findByToken(String token) {
        return passwordResetRepository.findByToken(token);
    }

    @Override
    public PasswordEmailReset save(Long userId, String token, Instant expirationTime) {
        PasswordEmailReset passwordResetToken = new PasswordEmailReset();
        passwordResetToken.setUserId(userId);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpirationTime(expirationTime);
        return passwordResetRepository.save(passwordResetToken);
    }

    @Override
    public Optional<PasswordEmailReset> update(Long id, PasswordEmailReset passwordReset) {
        Optional<PasswordEmailReset> passwordResetDb = passwordResetRepository.findById(id);
        if (passwordResetDb.isPresent()) {
            PasswordEmailReset updatePasswordReset = passwordResetDb.get();
            return Optional.of(passwordResetRepository.save(updatePasswordReset));
        }
        return passwordResetDb;
    }

    @Override
    public Optional<PasswordEmailReset> delete(Long id) {
        Optional<PasswordEmailReset> passwordReset = passwordResetRepository.findById(id);
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
