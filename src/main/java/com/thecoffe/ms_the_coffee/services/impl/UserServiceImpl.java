package com.thecoffe.ms_the_coffee.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thecoffe.ms_the_coffee.models.Role;
import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.models.UserRole;
import com.thecoffe.ms_the_coffee.repositories.RoleRepository;
import com.thecoffe.ms_the_coffee.repositories.UserRepository;
import com.thecoffe.ms_the_coffee.services.interfaces.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    // * Get all users
    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    // * Get user by id
    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // * Save user in databse with role USER or ADMIN
    @Transactional
    @Override
    public User save(User user) {
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRoleUser.ifPresent(role -> roles.add(role));
        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(role -> roles.add(role));
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // * Validate if username exists
    @Transactional
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // * Validate if exists email
    @Transactional
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // * Get user by id and update with new data
    @Transactional
    @Override
    public Optional<User> update(Long id, User user) {
        Optional<User> userDb = userRepository.findById(id);
        if (userDb.isPresent()) {
            User updateUser = userDb.get();
            updateUser.setUsername(user.getUsername());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return Optional.of(userRepository.save(updateUser));
        }
        return userDb;
    }

    // * Add ROLE_ADMIN to user
    @Transactional
    @Override
    public Optional<User> updateRoleAdmin(UserRole userRole) {
        Optional<User> userDb = userRepository.findByUsername(userRole.getUsername());
        User user = userDb.get();
        List<Role> roles = user.getRoles();
        List<String> roleName = roles.stream().map(Role::getName).collect(Collectors.toList());
        if (!roleName.contains("ROLE_ADMIN")) {
            Optional<Role> optionalRole = roleRepository.findByName("ROLE_ADMIN");
            Role adminRole;
            if (optionalRole.isPresent()) {
                adminRole = optionalRole.get();
            } else {
                adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                adminRole = roleRepository.save(adminRole);
            }
            roles.add(adminRole);
            user.setRoles(roles);
            userRepository.save(user);
        }
        return Optional.of(user);
    }

    // * Remove ROLE_ADMIN to user
    @Transactional
    @Override
    public Optional<User> updateRoleUser(UserRole userRole) {
        Optional<User> userDb = userRepository.findByUsername(userRole.getUsername());
        User user = userDb.get();
        List<Role> roles = user.getRoles();
        List<String> roleName = roles.stream().map(Role::getName).collect(Collectors.toList());
        if (roleName.contains("ROLE_ADMIN")) {
            Optional<Role> optionalRole = roleRepository.findByName("ROLE_ADMIN");
            if (optionalRole.isPresent()) {
                Role adminRole = optionalRole.get();
                roles.remove(adminRole);
                user.setRoles(roles);
                userRepository.save(user);
            }
        }
        return Optional.of(user);
    }

    // * Delete user by id
    @Transactional
    @Override
    public Optional<User> delete(Long id) {
        Optional<User> userDb = userRepository.findById(id);
        userDb.ifPresent((user) -> {
            userRepository.delete(user);
        });
        return userDb;
    }

}
