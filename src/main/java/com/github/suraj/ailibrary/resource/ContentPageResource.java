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
    private ContentPageServiceImpl contentPageService;

    @RequestMapping("/list")
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

    @RequestMapping("/get/{id}")
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
        return ResponseEntity.ok(
          Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("ContentPage",contentPage))
                .message("saved new content page")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteContentPage(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("ContentPage",contentPageService.deleteContentPage(id)))
                        .message("Deleted content page with id:"+id)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
