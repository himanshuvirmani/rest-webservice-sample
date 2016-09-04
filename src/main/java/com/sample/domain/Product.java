package com.sample.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by himanshu.virmani on 04/09/16.
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Product extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "details")
    private String details;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "quantity_unit")
    private String quantity_unit;

    @Column(name = "is_active")
    @JsonProperty(value = "is_active")
    private Boolean isActive;

    @Column(name = "ean_code")
    private String eanCode;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
