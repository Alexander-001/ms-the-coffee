package com.thecoffe.ms_the_coffee.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.thecoffe.ms_the_coffee.models.Address;

public interface AddressService {

    List<Address> findAll();

    Optional<Address> findById(Long id);

    Address save(Address address);

    Optional<Address> update(Long id, Address address);

    Optional<Address> delete(Long id);

    boolean existsByName(String name);
}
