package com.github.suraj.ailibrary.service.services;

import com.github.suraj.ailibrary.model.Category;
import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    // 1. Create new category
    Category createCategory (String name);

    // 2. Get a category by id
    Optional<Category> getCategoryById(Long id);

    //3. Get all Categories
    List<Category> getAllCategories();

    // 4. Delete Category
    Boolean deleteCategory (Category category);

    // 5. Update Category
    Category updateCategoryNameFromTo(String oldName, String newName);

    // 6. Get Category by name
    Optional<Category> getCategoryByName(String name);

    // 7. Add ContentPage to Category of
    PageOrderEntry addPageToCategory(ContentPage page, Category category);

    // 8. Get All ContentPages in Category
    List<ContentPage> getAllPagesWithCategoryName(String name);

}
