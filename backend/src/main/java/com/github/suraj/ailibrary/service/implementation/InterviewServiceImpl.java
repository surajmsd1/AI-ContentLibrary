package com.github.suraj.ailibrary.service.implementation;

import com.github.suraj.ailibrary.model.GptAPI.Analysis.Interview;
import com.github.suraj.ailibrary.model.GptAPI.Analysis.InterviewQuestion;
import com.github.suraj.ailibrary.repository.InterviewRepo;
import com.github.suraj.ailibrary.service.services.InterviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepo interviewRepo;

    public Interview createAndInitInterview(List<String> questions){
        Interview newInterview = new Interview();
        for (String question:questions) {
            InterviewQuestion newQuestion = new InterviewQuestion();
            newQuestion.setQuestion(question);
            newInterview.addQuestion(newQuestion);
        }
        return interviewRepo.save(newInterview);
    }
}
