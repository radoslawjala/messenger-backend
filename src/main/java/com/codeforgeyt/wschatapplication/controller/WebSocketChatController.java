package com.codeforgeyt.wschatapplication.controller;

import com.codeforgeyt.wschatapplication.dto.ChatMessage;
import com.codeforgeyt.wschatapplication.util.ActiveUserChangeListener;
import com.codeforgeyt.wschatapplication.util.ActiveUserManager;
import com.codeforgeyt.wschatapplication.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;

@Controller
@Log4j2
public class WebSocketChatController implements ActiveUserChangeListener {

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private ActiveUserManager activeUserManager;

    @PostConstruct
    private void init() {
        activeUserManager.registerListener(this);
    }

    @PreDestroy
    private void destroy() {
        activeUserManager.removeListener(this);
    }

    @MessageMapping("/chat")
    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessage message) {
        if(message.getRecipient() != null) {
            message.setTime(StringUtils.getCurrentFormattedTime(Long.parseLong(message.getTime())));
            String sender = sha.getUser().getName();
            log.info("Sender name from SimpMessageHeader: " + sender);
            if (senderIsNotRecipient(message.getRecipient(), sender)) {
                webSocket.convertAndSendToUser(sender, "/queue/messages", message);
            }
            webSocket.convertAndSendToUser(message.getRecipient(), "/queue/messages", message);
            log.info("Received message: " + message);
        } else {
            log.error("Recipient name is null");
        }
    }

    private boolean senderIsNotRecipient(String recipient, String sender) {
        return !sender.equals(recipient);
    }

    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}

