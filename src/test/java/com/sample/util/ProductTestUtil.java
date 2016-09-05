package com.sample.util;

import com.sample.domain.Category;
import com.sample.domain.Product;
import com.sample.web.rest.dto.ProductRequestDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by himanshu.virmani on 05/09/16.
 */
public class ProductTestUtil {

    public static List<Product> createProductListData(Long id, String title, BigDecimal price,long categoryId,String categoryName, Double quantity,String quantityUnit, String eanCode) {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setQuantityUnit(quantityUnit);
        product.setQuantity(quantity);
        product.setEanCode(eanCode);
        product.setCategory(createDummyCategory(categoryId, categoryName));
        products.add(product);
        return products;
    }

    public static List<ProductRequestDto> createProductRequest(String title, BigDecimal price,long categoryId,Double quantity,String quantityUnit, String eanCode) {
        List<ProductRequestDto> products = new ArrayList<>();
        ProductRequestDto product = new ProductRequestDto();
        product.setTitle(title);
        product.setPrice(price);
        product.setQuantityUnit(quantityUnit);
        product.setQuantity(quantity);
        product.setEanCode(eanCode);
        product.setCategoryId(categoryId);
        products.add(product);
        return products;
    }

    public static Category createDummyCategory(Long categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        category.setDescription(categoryName);
        return category;
    }
}
