package com.thecoffe.ms_the_coffee.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.thecoffe.ms_the_coffee.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
