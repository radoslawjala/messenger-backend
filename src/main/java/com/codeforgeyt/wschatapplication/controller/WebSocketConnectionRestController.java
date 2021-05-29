package com.codeforgeyt.wschatapplication.controller;

import com.codeforgeyt.wschatapplication.dto.MessageResponse;
import com.codeforgeyt.wschatapplication.dto.User;
import com.codeforgeyt.wschatapplication.util.ActiveUserManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin
@Log4j2
public class WebSocketConnectionRestController {

    @Autowired
    private ActiveUserManager activeSessionManager;

    @PostMapping("/rest/user-connect")
    public ResponseEntity<?> userConnect(@RequestBody User user, HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("Remote_Addr");
            if (StringUtils.isEmpty(remoteAddr)) {
                remoteAddr = request.getHeader("X-FORWARDED-FOR");
                if (remoteAddr == null || "".equals(remoteAddr)) {
                    remoteAddr = request.getRemoteAddr();
                }
            }
        }
        activeSessionManager.add(user.getUsername(), remoteAddr);
        log.info("User " + user.getUsername() + " connected");
        return ResponseEntity.ok(new MessageResponse("User " + user.getUsername() + " connected"));
    }

    @PostMapping("/rest/user-disconnect")
    public ResponseEntity<?> userDisconnect(@RequestBody User user) {
        activeSessionManager.remove(user.getUsername());
        log.info("User " + user.getUsername() + " disconnected");
        return ResponseEntity.ok(new MessageResponse("User " + user.getUsername() + " disconnected"));
    }

    @GetMapping("/rest/active-users-except/{userName}")
    public ResponseEntity<?> getActiveUsersExceptCurrentUser(@PathVariable String userName) {
        ArrayList<String> users = new ArrayList<>(activeSessionManager.getActiveUsersExceptCurrentUser(userName));
        log.info("Active users: " + users);
        return ResponseEntity.ok(users);
    }
}

