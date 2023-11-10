package com.github.suraj.ailibrary.model.GptAPI.Analysis;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @ToString(exclude = "questions") @RequiredArgsConstructor
@Table(name="interview")
@AllArgsConstructor
public class Interview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interview_id;
//    private String name;
    private String overallAnalysis;
    private int totalScore;
     @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
     private Set<InterviewQuestion> questions = new HashSet<>();

    public void addQuestion(InterviewQuestion question) {
        questions.add(question);
        question.setInterview(this);
    }
}



    //save question
        //table with question column
    //save answer later, maybe change answer
        //table with answer column plus a way to identify table
        //
    //save gpt analysis and scoring per questions and total, later
        //analysis and scoring column
    //Identify interview collection with an id
        //generate id before saving all questions and send to frontend.


