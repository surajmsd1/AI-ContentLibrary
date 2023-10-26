package com.github.suraj.ailibrary.service.implementation;

import com.github.suraj.ailibrary.model.Category;
import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;
import com.github.suraj.ailibrary.repository.CategoryRepo;
import com.github.suraj.ailibrary.repository.PageOrderEntryRepo;
import com.github.suraj.ailibrary.service.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final PageOrderEntryRepo pageOrderEntryRepo;
    @Override
    public Category createCategory(String name) {
        Category newCategory = new Category();
        newCategory.setName(name);
        log.info("Created and saved new category with name: {}", name);
        return categoryRepo.save(newCategory);
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        log.info("Fetching Category by id: {}",id);
        return categoryRepo.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        log.info("Fetching all Categories");
        return categoryRepo.findAll();
    }

    @Override
    public Boolean deleteCategory(Category category) {
        Boolean categoryExists = categoryRepo.findById(category.getId()).isPresent();
        if(categoryExists) {
            log.info("Deleting Category with name: {}", category.getName());
            categoryRepo.delete(category);
            int pagesRemoved = pageOrderEntryRepo.deleteAllEntriesWithCategoryName(category.getName());
            log.info("Category had {} pages associated with this category", pagesRemoved);
        }
        return categoryExists;
    }

    @Override
    public Category updateCategoryNameFromTo(String oldName, String newName) {
        Optional<Category> category = categoryRepo.findByName(oldName);
        if(category.isPresent()){
            log.info("Updating Category of name: {} To {}",oldName, newName);
            category.get().setName(newName);
            return categoryRepo.save(category.get());
        }
        log.warn("Category with name: {} could not be found", oldName);
        return null;
    }

    //maybe return category instead of optional
    @Override
    public Optional<Category> getCategoryByName(String name) {
        log.info("Fetching Category by name of: {}", name);
        return categoryRepo.findByName(name);
    }

    @Override
    public PageOrderEntry addPageToCategory(ContentPage page, Category category) {
        log.info("Adding page to category with name: {}", category.getName());

        PageOrderEntry newEntry = new PageOrderEntry();
        newEntry.setContentPage(page);
        newEntry.setCategory(category);

        return pageOrderEntryRepo.save(newEntry);
    }

    @Override
    public List<ContentPage> getAllPagesWithCategoryName(String name) {
        log.info("Fetching all pages for Category with name: {}", name);
        Optional<Category> category = categoryRepo.findByName(name);

        if (category.isEmpty()) {
            log.warn("No category found with name: {}", name);
            return Collections.emptyList();
        }
        return pageOrderEntryRepo.findContentPagesByCategoryName(name);
    }
}
