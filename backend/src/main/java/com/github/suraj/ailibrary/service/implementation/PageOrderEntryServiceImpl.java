package com.github.suraj.ailibrary.service.implementation;

import com.github.suraj.ailibrary.model.Category;
import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;
import com.github.suraj.ailibrary.repository.CategoryRepo;
import com.github.suraj.ailibrary.repository.PageOrderEntryRepo;
import com.github.suraj.ailibrary.service.services.PageOrderEntryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PageOrderEntryServiceImpl implements PageOrderEntryService {
    private final PageOrderEntryRepo pageOrderEntryRepo;
    private final CategoryRepo categoryRepo;

    public PageOrderEntry addPageToCategory(ContentPage contentPage, Category category) {
        log.info("Adding page to category with name: {}", category.getName());

        PageOrderEntry newEntry = new PageOrderEntry();
        newEntry.setContentPage(contentPage);
        newEntry.setCategory(category);

        List<PageOrderEntry> pages = category.getPages();
        newEntry.setSequenceIndex(pages.size());
        pages.add(newEntry);

        category.setPages(pages);
        categoryRepo.save(category);

        return pageOrderEntryRepo.save(newEntry);
    }

    @Override
    public PageOrderEntry addPageToCategory(ContentPage contentPage, Category category, int sequenceIndex) {
        log.info("Adding page to category with name: {} in sequence index of: {}", category.getName(), sequenceIndex);

        PageOrderEntry newEntry = new PageOrderEntry();
        newEntry.setContentPage(contentPage);
        newEntry.setCategory(category);

        List<PageOrderEntry> pages = category.getPages();

        // Check sequenceIndex bounds. If it's out of bounds, add to the end.
        if (sequenceIndex < 0 || sequenceIndex >= pages.size()) {
            newEntry.setSequenceIndex(pages.size());
            pages.add(newEntry);
        } else {
            // Shift subsequent entries' sequenceIndices to accommodate the new entry.
            for (int i = sequenceIndex; i < pages.size(); i++) {
                pages.get(i).setSequenceIndex(i + 1);
            }
            newEntry.setSequenceIndex(sequenceIndex);
            pages.add(sequenceIndex, newEntry);
        }

        category.setPages(pages);
        categoryRepo.save(category);

        return pageOrderEntryRepo.save(newEntry);
    }


    @Override
    public Optional<PageOrderEntry> getEntryById(Long id) {
        log.info("Fetching PageOrderEntry by id: {}", id);
        return pageOrderEntryRepo.findById(id);
    }

    @Override
    public Boolean deleteEntryById(Long id) {
        Boolean associationExists = pageOrderEntryRepo.findById(id).isPresent();
        if(associationExists){
            log.info("Deleting entry");
            pageOrderEntryRepo.deleteById(id);
        }else{
            log.info("Entry doesn't exist");
        }
        return associationExists;
    }

    //might need
//    @Override
//    public Boolean deleteEntryByEntry(PageOrderEntry entry) {
//        Boolean associationExists = pageOrderEntryRepo.fi(entry);
//        if(associationExists){
//            log.info("Deleting entry");
//            pageOrderEntryRepo.deleteById(id);
//        }else{
//            log.info("Entry doesn't exist");
//        }
//        return associationExists;
//    }
}
