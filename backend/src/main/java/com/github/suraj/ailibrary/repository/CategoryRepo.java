package com.github.suraj.ailibrary.repository;

import com.github.suraj.ailibrary.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

}
