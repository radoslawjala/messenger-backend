package com.codeforgeyt.wschatapplication.controller;

import com.codeforgeyt.wschatapplication.dto.ChatMessage;
import com.codeforgeyt.wschatapplication.util.ActiveUserChangeListener;
import com.codeforgeyt.wschatapplication.util.ActiveUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Set;

@Controller
public class WebSocketChatController implements ActiveUserChangeListener {

    // private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketChatController.class);

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

//    @GetMapping("/sockjs-message")
//    public String getWebSocketWithSockJs() {
//        return "sockjs-message";
//    }

    @MessageMapping("/chat")
    public void send(SimpMessageHeaderAccessor sha, @Payload ChatMessage chatMessage) {
        System.out.println("received message: " + chatMessage);
        String sender = sha.getUser().getName();
        System.out.println("Sender name: " + sender);
        ChatMessage message = new ChatMessage(chatMessage.getFrom(), chatMessage.getText(), chatMessage.getRecipient());
        if (!sender.equals(chatMessage.getRecipient())) {
            webSocket.convertAndSendToUser(sender, "/queue/messages", message);
        }
        System.out.println("Recipient name: " + chatMessage.getRecipient());
        webSocket.convertAndSendToUser(chatMessage.getRecipient(), "/queue/messages", message);
    }

    @Override
    public void notifyActiveUserChange() {
        Set<String> activeUsers = activeUserManager.getAll();
        webSocket.convertAndSend("/topic/active", activeUsers);
    }
}
