/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample.web.rest;

import com.sample.service.ProductService;
import com.sample.web.rest.dto.ProductRequestDto;
import com.sample.web.rest.dto.ProductResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ProductResponseDto> create(@RequestBody List<ProductRequestDto> productDtos) {
        List<ProductResponseDto> result = this.productService.addProducts(productDtos);
        return result;
    }

    // This can be paginated. Will do if I get time.
    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAll() {
        return this.productService.getAll();
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional(readOnly = true)
    public ProductResponseDto getById(@PathVariable("product_id") Long categoryId) {
        return this.productService.getProductById(categoryId);
    }

    @RequestMapping(value = "/{product_id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductResponseDto update(@PathVariable("product_id") Long categoryId, @RequestBody ProductRequestDto productDto) {
        ProductResponseDto result = this.productService.update(categoryId, productDto);
        return result;
    }

}
