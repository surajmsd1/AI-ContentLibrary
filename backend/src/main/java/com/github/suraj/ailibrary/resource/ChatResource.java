package com.github.suraj.ailibrary.resource;

import com.github.suraj.ailibrary.model.GptAPI.ChatRequest;
import com.github.suraj.ailibrary.model.GptAPI.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class ChatResource {

    @Autowired
    @Qualifier("openaiRestTemplate")
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String prompt) {
        System.out.println("This is the prompt: "+prompt);
        try {
            // create a request
            ChatRequest request = new ChatRequest(model, prompt);

            // call the API
            ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No response");
            }

            // return the first response
            return ResponseEntity.ok(response.getChoices().get(0).getMessage().getContent());
        } catch (HttpClientErrorException ex) {
            // Log the full response for debugging
            System.err.println("Error response body: " + ex.getResponseBodyAsString());
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        } catch (Exception ex) {
            // Log the full exception
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
