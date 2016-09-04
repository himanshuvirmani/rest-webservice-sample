package com.sample.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.domain.Product;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by himanshu.virmani on 04/09/16.
 */
@Data
public class ProductResponseDto {

    private Long id;

    private String title;

    private String details;

    private Double quantity;

    @JsonProperty(value = "quantity_unit")
    private String quantityUnit;

    @JsonProperty(value = "is_active")
    private Boolean isActive;

    @JsonProperty(value = "ean_code")
    private String eanCode;

    private BigDecimal price;

    @JsonProperty(value = "category")
    private String category;

    public ProductResponseDto(Product product) {
        this.title = product.getTitle();
        this.details = product.getDetails();
        this.category = product.getCategory().getName();
        this.quantity = product.getQuantity();
        this.quantityUnit = product.getQuantityUnit();
        this.eanCode = product.getEanCode();
        this.price = product.getPrice();
        this.id = product.getId();
        this.isActive = product.getIsActive();
    }
}
