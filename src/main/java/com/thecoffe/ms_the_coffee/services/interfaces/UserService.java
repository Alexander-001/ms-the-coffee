package com.thecoffe.ms_the_coffee.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.models.UserRole;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User save(User user);

    Optional<User> update(Long id, User user);

    Optional<User> updateRoleAdmin(UserRole userRole);

    Optional<User> updateRoleUser(UserRole userRole);

    Optional<User> delete(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
