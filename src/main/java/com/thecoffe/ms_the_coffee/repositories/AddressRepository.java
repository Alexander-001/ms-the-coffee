package com.thecoffe.ms_the_coffee.repositories;

import org.springframework.data.repository.CrudRepository;

import com.thecoffe.ms_the_coffee.models.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
    boolean existsByName(String name);
}
