package com.github.suraj.ailibrary.repository;

import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.PageOrderEntry;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageOrderEntryRepo extends JpaRepository<PageOrderEntry, Long> {
    @Query("SELECT entry.contentPage " +
            "FROM PageOrderEntry entry " +
            "WHERE entry.category.name = :categoryName " +
            "ORDER BY entry.sequenceIndex")
    List<ContentPage> findContentPagesByCategoryName(String categoryName);

    @Query("SELECT entry " +
            "FROM PageOrderEntry entry " +
            "WHERE entry.category.name = :categoryName " +
            "ORDER BY entry.sequenceIndex")
    List<PageOrderEntry> findPageOrderEntriesByCategoryName(String categoryName);



    @Modifying
    @Transactional
    @Query("DELETE From PageOrderEntry entry " +
            "WHERE entry.category.name = :categoryName")
    int deleteAllEntriesWithCategoryName(String categoryName);

    @Modifying
    @Transactional
    @Query("DELETE From PageOrderEntry entry WHERE entry.contentPage.id = :contentPageId")
    void deleteAllEntriesRelatedToContentPage(Long contentPageId);

}
