package com.thecoffe.ms_the_coffee.services;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmail() throws Exception {
        Model model = new ExtendedModelMap();
        model.addAttribute("name", "Alexander");
        MimeMessage mimeMessage = mock(MimeMessage.class);
        Context context = new Context();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process("email", context)).thenReturn("<html><body><h1>Hello World</h1></body></html>");
        doNothing().when(javaMailSender).send(mimeMessage);
        emailService.sendEmail("alexander@correo.com", "Prueba de correo", "emailTemplate", model);
    }
}