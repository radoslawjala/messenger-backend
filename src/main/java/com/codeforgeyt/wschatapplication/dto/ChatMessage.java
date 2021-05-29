package com.codeforgeyt.wschatapplication.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage {

    private String from;
    private String text;
    private String recipient;
    private String time;

    public ChatMessage() {
    }

    public ChatMessage(String from, String text, String recipient, String time) {
        this.from = from;
        this.text = text;
        this.recipient = recipient;
        this.time = time;
    }
}
