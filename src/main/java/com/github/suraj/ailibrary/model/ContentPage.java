package com.github.suraj.ailibrary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "prompt cannot be empty")
    private String prompt;
    @Column(columnDefinition = "MEDIUMTEXT") //can store up to 16 megabytes sheesh
    private String response;
    private String modelType;

    private LocalDateTime date;
    private String reviewed;
    private Integer rating;

}
