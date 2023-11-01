package com.github.suraj.ailibrary.model.GptAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private List<Choice> choices;

    // constructors, getters and setters
    @Data
    public static class Choice {

        private int index;
        private Message message;

        // constructors, getters and setters
    }
}

