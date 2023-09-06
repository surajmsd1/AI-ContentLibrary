package com.github.suraj.ailibrary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentPage {
    @Id @GeneratedValue(strategy = AUTO)
    private Long id;
    @NotEmpty(message = "prompt cannot be empty")
    private String prompt;
    private String response;
    private String modelType;
    private LocalDateTime date;
    private Boolean reviewed;
    private int rating;




}
