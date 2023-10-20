package com.github.suraj.ailibrary.repository;

import com.github.suraj.ailibrary.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    Category findByName(String name);
}
