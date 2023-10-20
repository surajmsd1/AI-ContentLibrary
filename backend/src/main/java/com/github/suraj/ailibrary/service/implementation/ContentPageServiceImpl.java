package com.github.suraj.ailibrary.service.implementation;

import com.github.suraj.ailibrary.model.Category;
import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;
import com.github.suraj.ailibrary.repository.ContentPageRepo;
import com.github.suraj.ailibrary.service.services.ContentPageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ContentPageServiceImpl implements ContentPageService {
    private final ContentPageRepo contentPageRepo;
    @Override
    public ContentPage createContentPage(ContentPage contentPage) {
        log.info("Saving new ContentPage{}",contentPage.getPrompt());
        return contentPageRepo.save(contentPage);
    }

    @Override
    public Optional<ContentPage> getContentPageById(Long id) {
        log.info("Fetching ContentPage by {}",id);
        return contentPageRepo.findById(id);
    }

    @Override
    public List<ContentPage> getAllContentPages() {
        log.info("Fetching all ContentPages");
        return contentPageRepo.findAll();
    }

    @Override
    public ContentPage updateContentPage(ContentPage contentPage) {
        log.info("Updating ContentPage of id: {}", contentPage.getId());
        return contentPageRepo.save(contentPage);
    }

    @Override
    public Boolean deleteContentPage(Long id) {
        log.info("Deleting ContentPage of id: {}",id);
        Boolean pagePresent = contentPageRepo.findById(id).isPresent();
        if(pagePresent){contentPageRepo.deleteById(id);}
        return pagePresent;
    }

    @Override
    public List<ContentPage> searchByPromptOrResponse(String query) {
        log.info("Fetching ContentPages related to: {}",query);
        return contentPageRepo.searchByPromptOrResponse(query);
    }

}
