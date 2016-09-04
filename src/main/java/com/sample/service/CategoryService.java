package com.sample.service;


import com.sample.domain.Category;
import com.sample.web.rest.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<Category> addCategories(List<CategoryDto> categories);

    List<Category> getAllCategories();

    Category getCategoryById(Long categoryId);

    Category updateCategory(Long categoryId, CategoryDto categoryDto);
}
