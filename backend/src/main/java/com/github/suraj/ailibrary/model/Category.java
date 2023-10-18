package com.github.suraj.ailibrary.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="Categories")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Category name must not be empty!")
    private String category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", orphanRemoval = true)
    private List<PageOrderEntry> pages;

}

