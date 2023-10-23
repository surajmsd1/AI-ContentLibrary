package com.github.suraj.ailibrary.resource;

import com.github.suraj.ailibrary.service.implementation.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryResource {
    private final CategoryServiceImpl categoryServiceImpl;

    // Get all Categories

    // Get all Pages in category in order

    // Add (and create) page to Category

    // Create new category

    // Delete new category

    // edit Category (low priority)
}
