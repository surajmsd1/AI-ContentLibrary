package com.github.suraj.ailibrary.resource;

import com.github.suraj.ailibrary.model.ContentPage;
import com.github.suraj.ailibrary.model.Response;
import com.github.suraj.ailibrary.service.implementation.ContentPageServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/content-page")
@RequiredArgsConstructor
public class ContentPageResource {
    private final ContentPageServiceImpl contentPageService;

    @GetMapping("/list")
    public ResponseEntity<Response> getContentPages(){
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("Pages", contentPageService.getAllContentPages()))
                .message("Pages Retrieved")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getContentPage(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("ContentPages", contentPageService.getContentPageById(id)))
                        .message("Page with id:"+id+ " retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveNewContentPage(@RequestBody @Valid ContentPage contentPage){
        ContentPage savedContentPage = contentPageService.createContentPage(contentPage);
        return ResponseEntity.ok(
          Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("ContentPage",savedContentPage))
                .message("saved new content page")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }

    //if id found return no_content, else if id not found return not_found
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteContentPage(@PathVariable("id") Long id){
        if (contentPageService.deleteContentPage(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Content page with id:" + id + " not found.")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        }
    }

}
