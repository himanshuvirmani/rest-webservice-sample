package com.sample.service.impl;

import com.sample.domain.Category;
import com.sample.domain.Product;
import com.sample.helper.ProductServiceHelper;
import com.sample.repository.CategoryRepository;
import com.sample.repository.ProductRepository;
import com.sample.service.ProductService;
import com.sample.web.rest.dto.ProductRequestDto;
import com.sample.web.rest.dto.ProductResponseDto;
import com.sample.web.rest.errors.CategoryNotFoundException;
import com.sample.web.rest.errors.ProductNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductServiceHelper productServiceHelper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductServiceHelper productServiceHelper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productServiceHelper = productServiceHelper;
    }

    @Override
    public List<ProductResponseDto> addProducts(List<ProductRequestDto> productDtos) {
        Assert.notNull(productDtos);
        Assert.notEmpty(productDtos);
        if (productServiceHelper.isCurrencyConversionsRequired(productDtos)) {
            final Map<String,BigDecimal> currencyMap = productServiceHelper.getCurrencyMap();
            productServiceHelper.doCurrencyConversions(productDtos, currencyMap);
        }
        List<Product> products = productDtos.stream().map(productRequestDto -> mapProductFromDto(productRequestDto)).collect(Collectors.toList());
        return StreamSupport.stream(productRepository.save(products).spliterator(), false).map(product -> new ProductResponseDto(product)).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getAll() {
        Iterable<Product> iterable = productRepository.findAll();
        if (iterable == null) {
            return Collections.EMPTY_LIST;
        }
        return StreamSupport.stream(iterable.spliterator(), false).map(product -> new ProductResponseDto(product)).collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findOne(productId);
        if (product == null) throw new ProductNotFoundException("Product with id " + productId + "not found");
        return new ProductResponseDto(product);
    }


    // Currently not differentiating between not provided null or set as null.
    // Will do it if I get time.
    // not allowing updating currency in update api as of now
    @Override
    public ProductResponseDto update(Long productId, ProductRequestDto productDto) {
        Product product = productRepository.findOne(productId);
        if (product == null) throw new ProductNotFoundException("Product with id " + productId + "not found");
        if (productDto.getTitle() != null) product.setTitle(productDto.getTitle());
        if (productDto.getDetails() != null) product.setDetails(productDto.getDetails());
        if (productDto.getPrice() != null) product.setPrice(productDto.getPrice());
        if (productDto.getEanCode() != null) product.setEanCode(productDto.getEanCode());
        if (productDto.getIsActive() != null) product.setIsActive(productDto.getIsActive());
        if (productDto.getQuantity() != null) product.setQuantity(productDto.getQuantity());
        if (productDto.getTitle() != null) product.setTitle(productDto.getTitle());
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findOne(productDto.getCategoryId());
            if (category == null)
                throw new CategoryNotFoundException("Product Category with id " + productDto.getCategoryId() + "not found");
            product.setCategory(category);
        }
        return new ProductResponseDto(productRepository.save(product));
    }

    private Product mapProductFromDto(ProductRequestDto productDto) {
        final Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findOne(productDto.getCategoryId());
            if (category == null)
                throw new CategoryNotFoundException("Category with id " + productDto.getCategoryId() + "not found");
            product.setCategory(category);
        }
        return product;
    }

}
