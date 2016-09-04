package com.sample.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.domain.Category;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * Created by himanshu.virmani on 04/09/16.
 */
@Data
public class ProductRequestDto {

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

    @JsonProperty(value = "category_id")
    private Long categoryId;

}
