package com.github.suraj.ailibrary.model.GptAPI.Analysis;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter @ToString @RequiredArgsConstructor
@Table(name="interview_question")
@AllArgsConstructor
public class InterviewQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    @JsonBackReference
    private Interview interview;

    @NotNull(message = "Question cannot be null!")
    private String question;
    private String response;
    private String analysis;
    private int score;
}
