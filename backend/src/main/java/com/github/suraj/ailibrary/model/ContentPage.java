package com.github.suraj.ailibrary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "content_page")
public class ContentPage {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    //Title, Question, Topic
    @NotEmpty(message = "prompt cannot be empty")
    private String prompt;
    //Content, Answer
    //@NotEmpty(message = "Response cannot be empty")
    @Column(columnDefinition = "MEDIUMTEXT") //can store up to 16 megabytes
    private String response;
    //Ai Model used to generate content
    //@NotEmpty(message = "Please enter Model Type, Cannot be empty")
    private String modelType;
    private LocalDateTime date;
    //who reviewed the content
    //@NotEmpty(message = "Please enter who wrote this entry")
    private String reviewed;
    //Rating of the content page
    private Integer rating;
}
