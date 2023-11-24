package com.github.suraj.ailibrary.service.implementation;

import com.github.suraj.ailibrary.model.ContentPages.Category;
import com.github.suraj.ailibrary.model.ContentPages.ContentPage;
import com.github.suraj.ailibrary.model.ContentPages.PageOrderEntry;
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

    //add page to end of category
    public PageOrderEntry addPageToCategory(ContentPage contentPage, Category category) {
        log.info("Adding page to category with name: {}", category.getName());

        PageOrderEntry newEntry = new PageOrderEntry();
        newEntry.setContentPage(contentPage);
        newEntry.setCategory(category);
        int sizeOfCategory = pageOrderEntryRepo.findPageOrderEntriesByCategoryName(category.getName()).size();
        newEntry.setSequenceIndex(sizeOfCategory);

        return pageOrderEntryRepo.save(newEntry);
    }

    public PageOrderEntry addPageToCategoryAt(ContentPage contentPage, Category category, int sequenceIndex) {
        log.info("Adding page to category with name: {} in sequence index of: {}", category.getName(), sequenceIndex);

        PageOrderEntry newEntry = new PageOrderEntry();
        newEntry.setContentPage(contentPage);
        newEntry.setCategory(category);

        List<PageOrderEntry> pages = pageOrderEntryRepo.findPageOrderEntriesByCategoryName(category.getName());

        // Adjust sequence index if it's out of bounds
        sequenceIndex = Math.min(sequenceIndex, pages.size());
        //move to end if less than 0
        sequenceIndex = sequenceIndex<0 ? pages.size() : sequenceIndex;
        newEntry.setSequenceIndex(sequenceIndex);

        // Shift subsequent entries' sequenceIndices to accommodate the new entry.
        int finalSequenceIndex = sequenceIndex;
        pages.stream()
                .filter(entry -> entry.getSequenceIndex() >= finalSequenceIndex)
                .forEach(entry -> entry.setSequenceIndex(entry.getSequenceIndex() + 1));

        pageOrderEntryRepo.saveAll(pages);
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
