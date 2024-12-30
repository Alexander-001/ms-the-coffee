package com.thecoffe.ms_the_coffee.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.thecoffe.ms_the_coffee.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByRut(String rut);

    Optional<User> findByEmail(String email);

    Optional<User> findByRut(String rut);
}
