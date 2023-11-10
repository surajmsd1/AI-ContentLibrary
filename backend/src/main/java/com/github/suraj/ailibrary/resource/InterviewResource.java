package com.github.suraj.ailibrary.resource;

import com.github.suraj.ailibrary.model.GptAPI.Analysis.AnalysisRequest;
import com.github.suraj.ailibrary.model.GptAPI.Analysis.Interview;
import com.github.suraj.ailibrary.model.GptAPI.ChatRequest;
import com.github.suraj.ailibrary.model.GptAPI.ChatResponse;
import com.github.suraj.ailibrary.model.GptAPI.Message;
import com.github.suraj.ailibrary.service.implementation.InterviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class ChatResource {
    private final InterviewServiceImpl interviewServiceImpl;
    @Autowired
    @Qualifier("openaiRestTemplate")
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String prompt) {
        // create a request
        ChatRequest request = new ChatRequest(model, prompt);
        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No response");
        }
        // return the first response
        return ResponseEntity.ok(response.getChoices().get(0).getMessage().getContent());
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<List<String>> interview(@RequestBody String resume) {
        // create a request
        ChatRequest request = new ChatRequest(model);
        // add interview prompt
        String interviewPrompt = "You are an tech interviewer," +
                " given a resume/prompt you select all technologies mentioned in the resume/prompt. Then come up with technical questions" +
                " to ask and evaluate the interviewee on technical skills. Respond only with the 10 questions.";
        Message interviewInit = new Message("system", interviewPrompt);
        request.addMessage(interviewInit);
        // add Resume
        Message Resume = new Message("user",resume);
        request.addMessage(Resume);

        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
        // Return the first response as a list of questions
        String questions = response.getChoices().get(0).getMessage().getContent();
        List<String> questionList = splitQuestions(questions);
        //save questions to db
//        Interview interview =interviewServiceImpl.createAndInitInterview(questionList);
        return ResponseEntity.ok(questionList);
    }
    @PostMapping("/getAnalysis")
    public ResponseEntity<String> analysis(@RequestBody AnalysisRequest analysisRequest) {
        // create a request
        ChatRequest request = new ChatRequest(model);
        // add interview prompt
        String analysisPrompt = "You are a tech interview assistant. Given a question and answer evaluate how well the answer answers the question. " +
                "Rate the answer out of 10.";
        Message analysisInit = new Message("system", analysisPrompt);
        request.addMessage(analysisInit);
        // add Resume
        Message QAMessage = new Message("user","Question:"+analysisRequest.getQuestion()
                +"/n Answer:"+analysisRequest.getAnswer());
        request.addMessage(QAMessage);

        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving analysis");
        }
        // Return the first response as a list of questions
        String analysis = response.getChoices().get(0).getMessage().getContent();
        return ResponseEntity.ok(analysis);
    }

    private static List<String> splitQuestions(String text) {
        // Pattern for finding the numbers followed by a dot and a space, considering multiline.
        String pattern = "(?m)^\\d+\\.\\s";
        // Split the text using the pattern.
        String[] questions = text.split(pattern);
        // Convert the array to a list and remove any empty entries that might be a result of splitting.
        return Arrays.stream(questions)
                .filter(s -> !s.trim().isEmpty())
                .map(s -> s.trim())
                .collect(Collectors.toList());
    }
}
