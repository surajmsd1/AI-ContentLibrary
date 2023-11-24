package com.github.suraj.ailibrary.model.ContentPages;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "page_order_entry")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageOrderEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "page_id")
    private ContentPage contentPage;

    private Integer sequenceIndex;
}
