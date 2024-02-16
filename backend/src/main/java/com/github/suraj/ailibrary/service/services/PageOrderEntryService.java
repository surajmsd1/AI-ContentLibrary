package com.github.suraj.ailibrary.service.services;

import com.github.suraj.ailibrary.model.ContentPages.Category;
import com.github.suraj.ailibrary.model.ContentPages.ContentPage;
import com.github.suraj.ailibrary.model.ContentPages.PageOrderEntry;

import java.util.Optional;

public interface PageOrderEntryService {

    // 1. Create new order-category association
    PageOrderEntry addPageToCategoryAt(ContentPage page, Category category, int sequenceIndex);
    PageOrderEntry addPageToCategory(ContentPage contentPage, Category category);

    // 2. get Entry by id
    Optional<PageOrderEntry> getEntryById(Long id);

    //3. Delete order-category Association by ID
    Boolean deleteEntryById(Long id);




}
