package com.github.suraj.ailibrary.model.GptAPI;

import lombok.Data;

@Data
public class Message {

    private String role;
    private String content;

    public Message(String user, String prompt) {
        role = user;
        content = prompt;
    }

    // constructor, getters and setters

}

