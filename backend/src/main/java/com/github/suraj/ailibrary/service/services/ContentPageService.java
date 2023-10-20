package com.github.suraj.ailibrary.service.services;

import com.github.suraj.ailibrary.model.Category;
import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;

import java.util.List;
import java.util.Optional;

public interface ContentPageService {

    // 1. Create a new ContentPage
    ContentPage createContentPage(ContentPage contentPage);

    // 2. Retrieve a ContentPage by ID
    Optional<ContentPage> getContentPageById(Long id);

    // 3. Retrieve all ContentPages
    List<ContentPage> getAllContentPages();

    // 4. Update a ContentPage
    ContentPage updateContentPage(ContentPage contentPage);

    // 5. Delete a ContentPage by ID
    Boolean deleteContentPage(Long id);

    // 6. Search ContentPages by prompt or response
    List<ContentPage> searchByPromptOrResponse(String query);
}
