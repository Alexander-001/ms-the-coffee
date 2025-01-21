package com.thecoffe.ms_the_coffee.controllers;

import com.thecoffe.ms_the_coffee.models.Address;
import com.thecoffe.ms_the_coffee.services.interfaces.AddressService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @Mock
    private ValidationBindingResult validationBindingResult;

    @Mock
    private BindingResult bindingResult;


    @InjectMocks
    private AddressController addressController;

    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address();
        address.setId(1L);
        address.setName("Test Address");
        address.setDescription("Test Address");
        address.setLongitude(10000);
        address.setLatitude(20000);
        address.setImage("Test Address");
        validationBindingResult = new ValidationBindingResult();
    }

    @Test
    void getAllAddress() {
        when(addressService.findAll()).thenReturn(Collections.singletonList(address));
        ResponseEntity<Map<String, Object>> response = addressController.getAllAddress();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notGetAddressById() {
        when(addressService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = addressController.getAddressById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAddressById() {
        when(addressService.findById(1L)).thenReturn(Optional.of(address));
        ResponseEntity<Map<String, Object>> response = addressController.getAddressById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notCreateAddressErrors() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = addressController.createPlace(address, bindingResult);
        assertNull(response);
    }

    @Test
    void createAddress() {
        when(addressService.save(address)).thenReturn(address);
        ResponseEntity<Map<String, Object>> response = addressController.createPlace(address, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void notUpdateAddressErrors() {
        isErrorFields();
        ResponseEntity<Map<String, Object>> response = addressController.updateAddress(address, bindingResult, 1L);
        assertNull(response);
    }

    @Test
    void notUpdateAddress() {
        when(addressService.update(1L, address)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = addressController.updateAddress(address, bindingResult, 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateAddress() {
        when(addressService.update(1L, address)).thenReturn(Optional.of(address));
        ResponseEntity<Map<String, Object>> response = addressController.updateAddress(address, bindingResult, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void notDeleteAddress() {
        when(addressService.delete(1L)).thenReturn(Optional.empty());
        ResponseEntity<Map<String, Object>> response = addressController.deleteAddressById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAddress() {
        when(addressService.delete(1L)).thenReturn(Optional.of(address));
        ResponseEntity<Map<String, Object>> response = addressController.deleteAddressById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    void isErrorFields() {
        when(bindingResult.hasFieldErrors()).thenReturn(true);
        when(validationBindingResult.validation(bindingResult)).thenReturn(null);
    }
}