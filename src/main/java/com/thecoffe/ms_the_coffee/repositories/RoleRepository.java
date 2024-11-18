package com.thecoffe.ms_the_coffee.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.thecoffe.ms_the_coffee.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
