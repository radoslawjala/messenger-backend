package com.codeforgeyt.wschatapplication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    private String text;

    public MessageResponse() {
    }

    public MessageResponse(String text) {
        this.text = text;
    }
}
