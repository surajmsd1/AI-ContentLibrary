package com.github.suraj.ailibrary.repository;

import com.github.suraj.ailibrary.model.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentPageRepo extends JpaRepository<ContentPage,Long> {
    @Query(value = "SELECT * FROM ContentPage c WHERE MATCH(c.prompt, c.response) AGAINST(?1)", nativeQuery = true)
    List<ContentPage> searchByPromptOrResponse(String query);
}
