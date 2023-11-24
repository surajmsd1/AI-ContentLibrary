package com.github.suraj.ailibrary.resource;

import com.github.suraj.ailibrary.model.GptAPI.Analysis.AnalysisRequestDTO;
import com.github.suraj.ailibrary.model.GptAPI.Analysis.QAADTO;
import com.github.suraj.ailibrary.model.GptAPI.Analysis.QuestionsRequestDTO;
import com.github.suraj.ailibrary.model.GptAPI.ChatRequest;
import com.github.suraj.ailibrary.model.GptAPI.ChatResponse;
import com.github.suraj.ailibrary.model.GptAPI.Message;
import com.github.suraj.ailibrary.service.implementation.InterviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewResource {
    private final InterviewServiceImpl interviewServiceImpl;
    @Autowired
    @Qualifier("openaiRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("openaiAudioRestTemplate")
    private RestTemplate restAudioTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

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
    public ResponseEntity<List<String>> interview(@RequestBody QuestionsRequestDTO requestDTO) {
        // create a request
        ChatRequest request = new ChatRequest(model);
        // add interview prompt
        String interviewPrompt = "You are a tech interviewer." +
                " Your task is to analyze the technologies listed in a resume/prompt." +
                " Based on the technologies and skills mentioned, generate exactly "+ requestDTO.getDifficulty() +"10 technical interview questions." +
                " The questions should be focused on evaluating the candidate's " +
                " knowledge of their technology stack, ability to work, and work experience." +
                " Provide only the questions, with no additional explanations or content.";

        Message interviewInit = new Message("system", interviewPrompt);
        request.addMessage(interviewInit);
        // add Resume
        Message Resume = new Message("user", requestDTO.getResume());
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
        //Interview interview =interviewServiceImpl.createAndInitInterview(questionList);
        return ResponseEntity.ok(questionList);
    }

    @PostMapping("/getAnalysis")
    public ResponseEntity<String> analysis(@RequestBody AnalysisRequestDTO analysisRequest) {
        // create a request
        ChatRequest request = new ChatRequest(model);
        // add interview prompt
        String analysisPrompt = "You are a tech interview assistant. Given a question and answer transcribed from audio, evaluate how well they answered the question. " +
                "Keep in mind that the interviewees can only speak the answer, so don't take off for their inability to show you clear code. " +
                "Rate the answer out of 10 in the following format: <Rating: #/10>.";
        Message analysisInit = new Message("system", analysisPrompt);
        request.addMessage(analysisInit);
        // add Resume
        Message QAMessage = new Message("user", "Question:" + analysisRequest.getQuestion()
                + "\n Answer:" + analysisRequest.getAnswer());
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

    @PostMapping("/getOverallAnalysis")
    public ResponseEntity<String> getOverAllAnalysis(@RequestBody List<QAADTO> QAAlist ){
        ChatRequest request = new ChatRequest(model);
        String overallInit = "Write a report on how well the interviewee understands their technology stack using the answers and analysis to the questions. Interviewee skipped the question if answer and analysis is blank" +
                "What software job level is the interviewee at?. Add the overall score they received.";
        Message prompt  = new Message("system",overallInit);
        request.addMessage(prompt);

        for (QAADTO each : QAAlist) {
            System.out.println("Question: "+each.question+"\nAnswer: "+each.answer+"\nAnalysis : "+each.analysis);
            request.addMessage(new Message("user","\nQuestion: "+each.question+"\nAnswer: "+each.answer+"Analysis : "+each.analysis));
        }
        ChatResponse result = restTemplate.postForObject(apiUrl,request,ChatResponse.class);
        String overallAnalysis =result.getChoices().get(0).getMessage().getContent();
        System.err.println(overallAnalysis);
        return ResponseEntity.ok(overallAnalysis);
    }

    @PostMapping("/speech-to-text")
    public ResponseEntity<String> speechToText(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
        }
        // Save the MultipartFile to a temporary file
        File tempFile = File.createTempFile("upload", ".wav");
        file.transferTo(tempFile);

        // Create a FileSystemResource from the temporary file
        FileSystemResource fileResource = new FileSystemResource(tempFile);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("model", "whisper-1");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String url = "https://api.openai.com/v1/audio/transcriptions";
        String response = restAudioTemplate.postForObject(url, requestEntity, String.class);
        // Clean up the temporary file
        tempFile.delete();
        System.err.println(response);
        return ResponseEntity.ok(response);
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
