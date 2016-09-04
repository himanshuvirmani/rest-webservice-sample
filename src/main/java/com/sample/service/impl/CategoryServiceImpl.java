package com.sample.service.impl;

import com.sample.domain.Category;
import com.sample.repository.CategoryRepository;
import com.sample.service.CategoryService;
import com.sample.web.rest.dto.CategoryDto;
import com.sample.web.rest.errors.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component("categoryService")
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> addCategories(List<CategoryDto> categoryDtos) {
        Assert.notNull(categoryDtos);
        Assert.notEmpty(categoryDtos);
        List<Category> categories = new ArrayList<>();
        for (CategoryDto categoryDto : categoryDtos) {
            final Category category = new Category();
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
            if (categoryDto.getParentId() != null) {
                category.setParent(categoryRepository.findOne(categoryDto.getParentId()));
            }
            categories.add(category);
        }
        return StreamSupport.stream(categoryRepository.save(categories).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<Category> getAllCategories() {
        Iterable<Category> iterable = categoryRepository.findAll();
        if (iterable == null) {
            return Collections.EMPTY_LIST;
        }
        List<Category> categories = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
        return categories;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        if (category == null) throw new CategoryNotFoundException("Category with id " + categoryId + "not found");
        return category;
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findOne(categoryId);
        if (category == null) throw new CategoryNotFoundException("Category with id " + categoryId + "not found");
        if (categoryDto.getName() != null) category.setName(categoryDto.getName());
        if (categoryDto.getDescription() != null) category.setDescription(categoryDto.getDescription());
        if (categoryDto.getParentId() != null) {
            Category parent = categoryRepository.findOne(categoryDto.getParentId());
            if (parent == null)
                throw new CategoryNotFoundException("Parent Category with id " + categoryDto.getParentId() + "not found");
            category.setParent(categoryRepository.findOne(categoryDto.getParentId()));
        }
        return categoryRepository.save(category);
    }
}
