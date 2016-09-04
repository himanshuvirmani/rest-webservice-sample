package com.sample.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by himanshu.virmani on 04/09/16.
 */
public class Category extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty(value = "products")
    private List<Product> products = new ArrayList<>();

    //for category path
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_id")
    private Category parent;

    // list of subcategories
    @OneToMany(mappedBy="parent")
    private Set<Category> categories = new HashSet<>();

}
