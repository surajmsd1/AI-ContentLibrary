package com.github.suraj.ailibrary.resource;

import com.github.suraj.ailibrary.model.ContentPages.Category;
import com.github.suraj.ailibrary.model.ContentPages.ContentPage;
import com.github.suraj.ailibrary.model.ContentPages.PageOrderEntry;
import com.github.suraj.ailibrary.model.ContentPages.Response;
import com.github.suraj.ailibrary.service.implementation.CategoryServiceImpl;
import com.github.suraj.ailibrary.service.implementation.ContentPageServiceImpl;
import com.github.suraj.ailibrary.service.implementation.PageOrderEntryServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryResource {
    private final CategoryServiceImpl categoryServiceImpl;
    private final ContentPageServiceImpl contentPageServiceImpl;
    private final PageOrderEntryServiceImpl pageOrderEntryServiceImpl;

    @GetMapping("/")
    public ResponseEntity<Response> getAllCategories() {
        List<Category> categories = categoryServiceImpl.getAllCategories();
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Retrieved all Categories")
                        .data(Map.of("Categories", categories))
                        .build()
        );
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<Response> getAllPagesInCategoryInOrder(@PathVariable String categoryName) {
        List<ContentPage> pages = categoryServiceImpl.getAllPagesWithCategoryName(categoryName);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Retrieved all ContentPages in Order")
                        .data(Map.of("ContentPages", pages))
                        .build()
        );
    }
    @Transactional
    @PostMapping("/{categoryName}/{contentPageId}")
    public ResponseEntity<Response> addPageToCategory(@PathVariable("categoryName") String categoryName,
                                                      @PathVariable("contentPageId") Long contentPageId) {
        Category category = categoryServiceImpl.getCategoryByName(categoryName)
                .orElseGet(() -> categoryServiceImpl.createCategory(categoryName));
        Optional<ContentPage> page = contentPageServiceImpl.getContentPageById(contentPageId);
        if(page.isPresent()){
            PageOrderEntry addedPage = pageOrderEntryServiceImpl.addPageToCategory(page.get(), category);
            List<ContentPage> categoryPages = categoryServiceImpl.getAllPagesWithCategoryName(categoryName);
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Adding content-page to "+categoryName+" Category")
                            .data(Map.of("ContentPages",categoryPages))
                            .build()
            );
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Content page with id: " + contentPageId + " not found.")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        }

    }

//    @GetMapping("/")
//    public ResponseEntity<Category> createNewCategory(@PathVariable ) {
//        Category createdCategory = categoryServiceImpl.createCategory(category.getName());
//        return ResponseEntity.ok(
//                Response.builder()
//                        .timeStamp(LocalDateTime.now())
//                        .status(HttpStatus.OK)
//                        .statusCode(HttpStatus.OK.value())
//                        .message("Retrieved all Categories")
//                        .data(Map.of("Categories", categories))
//                        .build()
//        );
//    }
//
//    @DeleteMapping("/{categoryId}")
//    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
//        boolean deleted = categoryService.deleteCategory(new Category(categoryId));
//        if (deleted) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


}
