package com.buchs.fullstack.controller;

import com.buchs.fullstack.entity.Message;
import com.buchs.fullstack.entity.UserInfo;
import com.buchs.fullstack.service.MessageService;
import com.buchs.fullstack.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
public class LikeController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/setLike")
    public ResponseEntity setLike(@RequestParam long messageId, Principal principal) {
        Message messageById = messageService.getMessageById(messageId);
        UserInfo user = userInfoService.getUserByName(principal.getName());
        Set<UserInfo> userLikes = messageById.getUserLikes();


        if (userLikes.contains(user)) {
            userLikes.remove(user);
            messageById.setLikeCount(messageById.getLikeCount() - 1l);

        } else {
            userLikes.add(user);
            messageById.setLikeCount(messageById.getLikeCount() + 1l);
        }
        messageService.saveMessage(messageById);


        messageService.saveMessage(messageById);
        return ResponseEntity.ok().build();
    }
};
