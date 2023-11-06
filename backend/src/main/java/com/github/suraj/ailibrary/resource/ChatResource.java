package com.github.suraj.ailibrary.resource;

import com.github.suraj.ailibrary.model.GptAPI.ChatRequest;
import com.github.suraj.ailibrary.model.GptAPI.ChatResponse;
import com.github.suraj.ailibrary.model.GptAPI.Message;
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
public class ChatResource {

    @Autowired
    @Qualifier("openaiRestTemplate")
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    public String interviewPrompt = "You are an tech interviewer," +
            " given a resume/prompt you come up with technical questions on the technologies" +
            " to ask and evaluate the interviewee. No comments only provide 10 questions.";

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
        Message interviewInit = new Message("system",interviewPrompt);
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
        return ResponseEntity.ok(questionList);
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
