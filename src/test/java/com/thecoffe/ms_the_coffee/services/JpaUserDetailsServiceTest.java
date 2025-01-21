package com.thecoffe.ms_the_coffee.services;

import com.thecoffe.ms_the_coffee.exceptions.EmailNotFoundException;
import com.thecoffe.ms_the_coffee.models.Role;
import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JpaUserDetailsServiceTest {


    @InjectMocks
    private JpaUserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setRut("rut1");
        user.setEmail("email1@gmail.com");
        user.setFirstName("first1");
        user.setLastName("last1");
        user.setPhone("phone1");
        user.setGender("male");
        user.setBirthDate("11/11/1111");
        user.setCountry("country1");
        user.setCity("city1");
        user.setAddress("address1");
        user.setPassword("password1");
        user.setPosition("position1");
        user.setTeam("team1");
        user.setImage("image1");
        user.setAdmin(true);

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
    }

    @Test
    void loadUserByUsernameEmpty() {
        when(userRepository.findByEmail("email@correo.com")).thenReturn(Optional.empty());
        EmailNotFoundException exception = assertThrows(EmailNotFoundException.class, () -> userDetailsService.loadUserByUsername("email@correo.com"));
        assertEquals("Correo: email@correo.com no existe en el sistema.", exception.getMessage());
    }

    @Test
    void  loadUserByUsername() {
        when(userRepository.findByEmail("email@correo.com")).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername("email@correo.com");
        assertNotNull(userDetails);
    }
}