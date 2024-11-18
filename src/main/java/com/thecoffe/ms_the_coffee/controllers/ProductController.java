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

import com.thecoffe.ms_the_coffee.models.Product;
import com.thecoffe.ms_the_coffee.services.ProductService;
import com.thecoffe.ms_the_coffee.validations.ValidationBindingResult;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ValidationBindingResult validationBindingResult;

    // * Get all products
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAllProducts() {
        List<Product> products = productService.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Productos encontrados");
        response.put("products", products);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // * Get product by id
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findProductById(@PathVariable Long id) {
        Optional<Product> productDb = productService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (productDb.isPresent()) {
            response.put("message", "Producto encontrado");
            response.put("product", productDb.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No existe el producto");
        response.put("product", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Create a new product
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody Product product,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Product newProduct = productService.save(product);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Producto creado correctamente");
        response.put("product", newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // * Update product by id
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@Valid @RequestBody Product product, BindingResult result,
            @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validationBindingResult.validation(result);
        }
        Optional<Product> updateProduct = productService.update(id, product);
        Map<String, Object> response = new HashMap<>();
        if (updateProduct.isPresent()) {
            product.setId(id);
            response.put("message", "Producto actualizado");
            response.put("product", updateProduct.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo actualizar dirección");
        response.put("address", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // * Delete product by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Optional<Product> productDelete = productService.delete(id);
        Map<String, Object> response = new HashMap<>();
        if (productDelete.isPresent()) {
            response.put("message", "Producto eliminado");
            response.put("product", productDelete.orElseThrow());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("message", "No se pudo eliminar producto");
        response.put("product", new HashMap<>());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
