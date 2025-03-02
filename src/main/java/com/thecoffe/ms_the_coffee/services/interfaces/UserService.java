package com.thecoffe.ms_the_coffee.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.models.UserRole;

public interface UserService {

    List<User> findAll();

    Optional<User> findByEmail(String email);

    User save(User user);

    Optional<User> update(Long id, User user);

    Optional<User> updateRoleAdmin(UserRole userRole);

    Optional<User> updateRoleUser(UserRole userRole);

    Optional<User> delete(Long id);

    boolean existsByEmail(String email);

    boolean existsByRut(String rut);

    boolean validatePasswords(String currentPassword, String newPassword);
}
