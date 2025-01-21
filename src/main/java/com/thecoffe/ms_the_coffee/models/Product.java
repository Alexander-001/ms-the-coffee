package com.thecoffe.ms_the_coffee.models;

import com.thecoffe.ms_the_coffee.validations.ExistsProduct;
import com.thecoffe.ms_the_coffee.validations.NotExistsCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacio.")
    @Size(min = 3, max = 20, message = "El nombre debe estar enre 3 y 20 carcateres.")
    private String name;
    @Min(value = 500, message = "El precio minimo es de 500.")
    @NotNull(message = "El precio no puede estar vacio.")
    private Integer price;
    @NotBlank(message = "La descripción no puede estar vacia.")
    private String description;
    @ExistsProduct
    @NotBlank(message = "El sku es requerido.")
    private String sku;
    @NotBlank(message = "La imagen en base 64 no puede estar vacia.")
    private String image;
    @NotBlank(message = "La categoria no puede estar vacia")
    @NotExistsCategory
    private String category;
    @NotNull(message = "El stock no puede estar vacio.")
    private Integer stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}
