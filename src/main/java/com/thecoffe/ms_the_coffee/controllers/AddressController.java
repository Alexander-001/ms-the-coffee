package com.thecoffe.ms_the_coffee.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thecoffe.ms_the_coffee.models.Address;
import com.thecoffe.ms_the_coffee.services.interfaces.AddressService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService placeService;

    @Autowired
    private ValidationBindingResult validationBindingResult;

    // * Get all address */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAddress() {
        List<Address> address = placeService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Direcciones encontradas");
        response.put("addresses", address);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // * Get addres by id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAddressById(@PathVariable Long id) {
        Optional<Address> addressDb = placeService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (addressDb.isPresent()) {
            response.put("message", "Dirección encontrada");
            response.put("address", addressDb.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No existe la dirección");
        response.put("address", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Create a new addres
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPlace(@Valid @RequestBody Address address, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Address newAddress = placeService.save(address);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Direccion creada correctamente");
        response.put("address", newAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // * Update addres by id
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAddress(@Valid @RequestBody Address address, BindingResult result,
            @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<Address> updateAddress = placeService.update(id, address);
        Map<String, Object> response = new HashMap<>();
        if (updateAddress.isPresent()) {
            address.setId(id);
            response.put("message", "Dirección actualizada");
            response.put("address", updateAddress.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo actualizar dirección");
        response.put("address", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Delete addres by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAddressById(@PathVariable Long id) {
        Optional<Address> addressDelete = placeService.delete(id);
        Map<String, Object> response = new HashMap<>();
        if (addressDelete.isPresent()) {
            response.put("message", "Dirección eliminada");
            response.put("address", addressDelete.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo eliminar dirección");
        response.put("address", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
