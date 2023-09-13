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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content-pages")
@RequiredArgsConstructor
public class ContentPageResource {
    private final ContentPageServiceImpl contentPageServiceImpl;

    @GetMapping("/")
    public ResponseEntity<Response> getContentPages(){
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("ContentPages", contentPageServiceImpl.getAllContentPages()))
                .message("Pages Retrieved")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getContentPage(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("ContentPage", contentPageServiceImpl.getContentPageById(id)))
                        .message("Page with id: "+id+ " retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/")
    public ResponseEntity<Response> saveNewContentPage(@RequestBody @Valid ContentPage contentPage){
        ContentPage savedContentPage = contentPageServiceImpl.createContentPage(contentPage);
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

    //if id found return status no_content, else if id not found return status not_found
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteContentPage(@PathVariable("id") Long id){
        if (contentPageServiceImpl.deleteContentPage(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Content page with id: " + id + " not found.")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        }
    }

    //GetMapping for search by query return status Not_found if zero results
    @GetMapping("/search")
    public ResponseEntity<Response> searchByQuery(@RequestParam String query){
        List<ContentPage> results = contentPageServiceImpl.searchByPromptOrResponse(query);
        if(results.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message("No Content Pages found with the query: "+query)
                        .build()
            );
        }else{
            return ResponseEntity.ok(
                    Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Retrieved all content pages related to the query: "+query)
                        .data(Map.of("ContentPages",results))
                    .build()
            );
        }
    }

}
