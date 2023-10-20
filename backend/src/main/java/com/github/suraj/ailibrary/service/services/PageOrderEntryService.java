package com.github.suraj.ailibrary.service.services;

import com.github.suraj.ailibrary.model.Category;
import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;

import java.util.Optional;

public interface PageOrderEntryService {

    // 1. Create new order-category association
    PageOrderEntry addPageToCategory(ContentPage page, Category category, int sequenceIndex);
    PageOrderEntry addPageToCategory(ContentPage contentPage, Category category);

    // 2. get Entry by id
    Optional<PageOrderEntry> getEntryById(Long id);

    //3. Delete order-category Association by ID
    Boolean deleteEntryById(Long id);




}
