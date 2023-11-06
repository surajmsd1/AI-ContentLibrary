package com.github.suraj.ailibrary.model.GptAPI;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;
//    private int n;
//    private double temperature;

    public ChatRequest(String model) {
        this.model = model;
        this.messages = new ArrayList<Message>();
    }

    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<Message>();
        this.messages.add(new Message("user", prompt));
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }

}
