package com.github.suraj.ailibrary.model;

public interface PageOrderEntryService {

    // 1. Create new order-category association
    PageOrderEntry createNewPageOrderEntry(ContentPage page, Category category);

    // 2. get Entry
    PageOrderEntry getEntryById(Long id);

    //3. Delete order-category Association by ID
    Boolean deleteAssociationById (Long id);

    //4. Delete order-category Association by Entry
    Boolean deleteAssociationByEntry (PageOrderEntry entry);

    //5.
}
