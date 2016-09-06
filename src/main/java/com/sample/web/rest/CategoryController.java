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

import com.sample.config.dbutils.ReadOnlyConnection;
import com.sample.domain.Category;
import com.sample.service.CategoryService;
import com.sample.web.rest.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Category> create(@RequestBody List<CategoryDto> categories) {
        List<Category> result = this.categoryService.addCategories(categories);
        return result;
    }

    // This can be paginated. Will do if I get time.
    @RequestMapping(value = "/", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return this.categoryService.getAllCategories();
    }

    @RequestMapping(value = "/{category_id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ReadOnlyConnection
    @Transactional(readOnly = true)
    public Category getById(@PathVariable("category_id") Long categoryId) {
        return this.categoryService.getCategoryById(categoryId);
    }

    @RequestMapping(value = "/{category_id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Category update(@PathVariable("category_id") Long categoryId, @RequestBody CategoryDto categoryDto) {
        Category result = this.categoryService.updateCategory(categoryId, categoryDto);
        return result;
    }

}
