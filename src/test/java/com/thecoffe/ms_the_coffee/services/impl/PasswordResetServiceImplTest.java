package com.thecoffe.ms_the_coffee.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thecoffe.ms_the_coffee.models.PasswordEmailReset;
import com.thecoffe.ms_the_coffee.repositories.PasswordEmailResetRepository;

class PasswordResetServiceImplTest {

    @Mock
    private PasswordEmailResetRepository passwordResetRepository;

    @InjectMocks
    private PasswordEmailResetServiceImpl passwordResetService;

    private PasswordEmailReset passwordReset;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordReset = new PasswordEmailReset();
        passwordReset.setId(1L);
        passwordReset.setToken("token");
        passwordReset.setUserId(1L);
        passwordReset.setExpirationTime(Instant.ofEpochSecond(1000));
    }

    @Test
    void findAll() {
        when(passwordResetRepository.findAll()).thenReturn(Collections.singletonList(passwordReset));
        assertEquals(passwordResetService.findAll(), Collections.singletonList(passwordReset));
    }

    @Test
    void findByToken() {
        when(passwordResetRepository.findByToken("token")).thenReturn(Optional.of(passwordReset));
        assertEquals(passwordResetService.findByToken("token"), Optional.of(passwordReset));
    }

    @Test
    void save() {
        when(passwordResetRepository.save(passwordReset)).thenReturn(passwordReset);
        PasswordEmailReset result = passwordResetService.save(passwordReset.getUserId(), passwordReset.getToken(),
                passwordReset.getExpirationTime());
        assertNull(result);
    }

    @Test
    void update() {
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.of(passwordReset));
        when(passwordResetRepository.save(passwordReset)).thenReturn(passwordReset);
        assertEquals(passwordResetService.update(1L, passwordReset), Optional.of(passwordReset));
    }

    @Test
    void delete() {
        when(passwordResetRepository.findById(1L)).thenReturn(Optional.of(passwordReset));
        doNothing().when(passwordResetRepository).delete(passwordReset);
        assertEquals(passwordResetService.delete(1L), Optional.of(passwordReset));
    }

    @Test
    void cleanExpiredTokens() {
        doNothing().when(passwordResetRepository).deleteExpiredTokens(Instant.now());
        passwordResetService.cleanExpiredTokens();
    }
}