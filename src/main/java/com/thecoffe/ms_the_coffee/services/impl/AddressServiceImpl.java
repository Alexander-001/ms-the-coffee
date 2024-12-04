package com.thecoffe.ms_the_coffee.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thecoffe.ms_the_coffee.models.Address;
import com.thecoffe.ms_the_coffee.repositories.AddressRepository;
import com.thecoffe.ms_the_coffee.services.interfaces.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // * Get all directions
    @Transactional(readOnly = true)
    @Override
    public List<Address> findAll() {
        return (List<Address>) addressRepository.findAll();
    }

    // * Get direction by id
    @Transactional(readOnly = true)
    @Override
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    // * Validate if exists direction by name in database
    @Transactional(readOnly = true)
    @Override
    public boolean existsByName(String name) {
        return addressRepository.existsByName(name);
    }

    // * Save new direction in database
    @Transactional
    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    // * Get direction by id and update with new data
    @Transactional
    @Override
    public Optional<Address> update(Long id, Address address) {
        Optional<Address> addressDb = addressRepository.findById(id);
        if (addressDb.isPresent()) {
            Address updateAddress = addressDb.get();
            updateAddress.setName(address.getName());
            updateAddress.setDescription(address.getDescription());
            updateAddress.setLatitude(address.getLatitude());
            updateAddress.setLongitude(address.getLongitude());
            updateAddress.setImage(address.getImage());
            return Optional.of(addressRepository.save(updateAddress));
        }
        return addressDb;
    }

    // * Delete direction by id
    @Transactional
    @Override
    public Optional<Address> delete(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        address.ifPresent(place -> {
            addressRepository.delete(place);
        });
        return address;
    }

}
