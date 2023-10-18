package com.github.suraj.ailibrary.service;

import com.github.suraj.ailibrary.model.Category;

import java.util.List;

public interface CategoryService {
    // 1. Create new category
    Category createCategory (Category category);

    // 2. Get a category by id
    Category getById(Long id);

    //3. Get all Categories
    List<Category> getAllCategories();

    // 4. Delete Category
    Boolean deleteCategory (Category category);

    // 5. Update Category
    Category updateCategory(Category category);

    // 6. Get Category by name
    Category getByName (String name);

}
