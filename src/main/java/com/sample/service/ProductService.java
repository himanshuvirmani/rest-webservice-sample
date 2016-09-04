package com.sample.service;


import com.sample.web.rest.dto.ProductRequestDto;
import com.sample.web.rest.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> addProducts(List<ProductRequestDto> productDtos);

    List<ProductResponseDto> getAll();

    ProductResponseDto getProductById(Long productId);

    ProductResponseDto update(Long productId, ProductRequestDto productDto);

}
