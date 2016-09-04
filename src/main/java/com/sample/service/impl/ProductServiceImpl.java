package com.sample.service.impl;

import com.sample.repository.ProductRepository;
import com.sample.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("productService")
@Transactional
public class ProductServiceImpl implements CategoryService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

}
