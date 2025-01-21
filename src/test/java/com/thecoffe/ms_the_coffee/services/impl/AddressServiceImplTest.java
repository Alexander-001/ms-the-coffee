package com.thecoffe.ms_the_coffee.services.impl;

import com.thecoffe.ms_the_coffee.models.Address;
import com.thecoffe.ms_the_coffee.repositories.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address();
        address.setId(1L);
        address.setName("Test Address");
        address.setDescription("Test Address");
        address.setLatitude(10000);
        address.setLongitude(10000);
        address.setImage("Test Address");

    }

    @Test
    void findAll() {
        when(addressRepository.findAll()).thenReturn(Collections.singletonList(address));
        assertEquals(addressService.findAll(), Collections.singletonList(address));
    }

    @Test
    void findById() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        assertEquals(addressService.findById(1L), Optional.of(address));
    }

    @Test
    void existsByName() {
        when(addressRepository.existsByName("Test Address")).thenReturn(true);
        assertTrue(addressService.existsByName("Test Address"));
    }

    @Test
    void save() {
        when(addressRepository.save(address)).thenReturn(address);
        assertEquals(addressService.save(address), address);
    }

    @Test
    void update() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);
        assertEquals(addressService.update(1L, address), Optional.of(address));
    }

    @Test
    void delete() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        doNothing().when(addressRepository).delete(address);
        assertEquals(addressService.delete(1L), Optional.of(address));
    }
}