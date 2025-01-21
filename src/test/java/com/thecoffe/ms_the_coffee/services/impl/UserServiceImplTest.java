package com.thecoffe.ms_the_coffee.services.impl;

import com.thecoffe.ms_the_coffee.models.Role;
import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.models.UserRole;
import com.thecoffe.ms_the_coffee.repositories.RoleRepository;
import com.thecoffe.ms_the_coffee.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private Role role;

    private UserRole userRole;

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

        role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));

        userRole = new UserRole();
        userRole.setEmail(user.getEmail());
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        assertEquals(userService.findAll(), Collections.singletonList(user));
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail("email1@gmail.com")).thenReturn(Optional.of(user));
        assertEquals(userService.findByEmail("email1@gmail.com"), Optional.of(user));
    }

    @Test
    void save() {
        when(userRepository.save(user)).thenReturn(user);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(user.getPassword())).thenReturn("password1");
        assertEquals(userService.save(user), user);
    }

    @Test
    void existsByEmail() {
        when(userRepository.existsByEmail("email1@gmail.com")).thenReturn(true);
        assertTrue(userService.existsByEmail("email1@gmail.com"));
    }

    @Test
    void existsByRut() {
        when(userRepository.existsByRut("rut1")).thenReturn(true);
        assertTrue(userService.existsByRut("rut1"));
    }

    @Test
    void update() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(userService.update(1L, user), Optional.of(user));
    }

    @Test
    void updateRoleAdmin_adminRole() {
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        when(userRepository.findByEmail("email1@gmail.com")).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));
        assertEquals(userService.updateRoleAdmin(userRole), Optional.of(user));
    }

    @Test
    void updateRoleAdmin_adminUser() {
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        when(userRepository.findByEmail("email1@gmail.com")).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        assertEquals(userService.updateRoleAdmin(userRole), Optional.of(user));
    }

    @Test
    void updateRoleUser() {
        when(userRepository.findByEmail("email1@gmail.com")).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(userService.updateRoleUser(userRole), Optional.of(user));
    }

    @Test
    void delete() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(roleRepository).deleteById(1L);
        doNothing().when(userRepository).deleteById(1L);
        assertEquals(userService.delete(1L), Optional.of(user));
    }
}