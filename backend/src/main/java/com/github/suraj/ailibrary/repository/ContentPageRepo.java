package com.github.suraj.ailibrary.repository;

import com.github.suraj.ailibrary.model.ContentPages.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentPageRepo extends JpaRepository<ContentPage, Long> {

    @Query(value = "SELECT c.*, MATCH(c.prompt, c.response) AGAINST(?1) AS relevance " +
            "FROM content_page c " +
            "WHERE MATCH(c.prompt, c.response) AGAINST(?1) " +
            "ORDER BY relevance DESC", nativeQuery = true)
    List<ContentPage> searchByPromptOrResponse(String query);
}

