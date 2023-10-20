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

import java.util.ArrayList;
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
    public Optional<Category> getById(Long id) {
        log.info("Fetching Category by id: {}",id);
        return categoryRepo.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        log.info("Fetching all Catergories");
        return categoryRepo.findAll();
    }

    @Override
    public Boolean deleteCategory(Category category) {
        Boolean categoryExists = categoryRepo.findById(category.getId()).isPresent();
        if(categoryExists) {
            log.info("Deleting Category with name: {}", category.getName());
            categoryRepo.delete(category);
        }
        return categoryExists;
    }

    @Override
    public Category updateCategory(Category category) {
        log.info("Updating Category of name: {}",category.getName());
        return categoryRepo.save(category);
    }

    @Override
    public Category getByName(String name) {
        log.info("Fetching Category by name of: {}", name);
        return categoryRepo.findByName(name);
    }
    @Override
    public PageOrderEntry addPageToCategory(ContentPage page, Category category) {
        log.info("Adding page to category with name: {}", category.getName());

        PageOrderEntry newEntry = new PageOrderEntry();
        newEntry.setContentPage(page);
        newEntry.setCategory(category);

        List<PageOrderEntry> pages = category.getPages();
        newEntry.setSequenceIndex(pages.size());  // Set the sequence index at the end.
        pages.add(newEntry);

        category.setPages(pages);
        categoryRepo.save(category);

        return pageOrderEntryRepo.save(newEntry);
    }

    @Override
    public List<ContentPage> getAllPages(String name) {
        log.info("Fetching all pages for Category with name: {}", name);
        Category category = categoryRepo.findByName(name);

        if (category == null) {
            log.warn("No category found with name: {}", name);
            return Collections.emptyList();
        }

        List<PageOrderEntry> pageEntries = category.getPages();
        List<ContentPage> contentPages = new ArrayList<>();
        for (PageOrderEntry entry : pageEntries) {
            contentPages.add(entry.getContentPage());
        }

        return contentPages;
    }
}
