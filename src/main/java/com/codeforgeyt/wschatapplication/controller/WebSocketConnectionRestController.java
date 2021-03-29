package com.codeforgeyt.wschatapplication.controller;

import com.codeforgeyt.wschatapplication.dto.MessageResponse;
import com.codeforgeyt.wschatapplication.dto.User;
import com.codeforgeyt.wschatapplication.util.ActiveUserManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin
public class WebSocketConnectionRestController {

    @Autowired
    private ActiveUserManager activeSessionManager;

//    @PostMapping(value = "/rest/user-connect")
//    @CrossOrigin
//    public void userConnect(@RequestBody String title) {
//
//        System.out.println(title);
//    }
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
//        System.out.println("username:" + user.getUserName() + ", remoteAddr: " + remoteAddr);
        activeSessionManager.add(user.getUsername(), remoteAddr);
        return ResponseEntity.ok(new MessageResponse("powinno być sikalafa żelafą żymbalade sur nymbła"));
    }

//    @GetMapping("/rest/user-connect/info")
//    public void tmp(HttpServletRequest request) {
//        System.out.println("riszki sziszki");
//        String remoteAddr = "";
//        if (request != null) {
//            remoteAddr = request.getHeader("Remote_Addr");
//            if (StringUtils.isEmpty(remoteAddr)) {
//                remoteAddr = request.getHeader("X-FORWARDED-FOR");
//                if (remoteAddr == null || "".equals(remoteAddr)) {
//                    remoteAddr = request.getRemoteAddr();
//                    System.out.println(remoteAddr);
//                }
//            }
//        }
//    }

    @PostMapping("/rest/user-disconnect")
    public String userDisconnect(@ModelAttribute("username") String userName) {
        activeSessionManager.remove(userName);
        return "disconnected";
    }

    @GetMapping("/rest/active-users-except/{userName}")
    public ResponseEntity<?> getActiveUsersExceptCurrentUser(@PathVariable String userName) {
        ArrayList<String> users = new ArrayList<>();
        for(String name: activeSessionManager.getActiveUsersExceptCurrentUser(userName)) {
            users.add(name);
        }
        System.out.println("Users list: " + users);
        return ResponseEntity.ok(users);
    }
}

