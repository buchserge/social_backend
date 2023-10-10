package com.buchs.fullstack.controller;

import com.buchs.fullstack.entity.Message;
import com.buchs.fullstack.entity.MessagesWrapper;
import com.buchs.fullstack.entity.UserInfo;
import com.buchs.fullstack.entity.dto.MessageDto;
import com.buchs.fullstack.entity.dto.UserDto;
import com.buchs.fullstack.repo.MessageRepo;
import com.buchs.fullstack.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MessageController {


    @Autowired
    private  MessageRepo messageRepo;
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/messages")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<MessagesWrapper> getMessages(Principal principal, @RequestParam(defaultValue = "0") int page) {
        UserInfo currentUser = userInfoService.getUserByName(principal.getName());
        List<Message> messages = currentUser.getMessages();

        List<MessageDto> collect =  messages.stream().map(m -> {
            MessageDto msgDto = new MessageDto(m.getId(), m.getName(), m.getText(), m.getLikeCount(),
                    m.getUserLikes().stream().map(usr -> {
                        UserDto usrDto = new UserDto(usr.getId(), usr.getName());
                        return usrDto;
                    }).collect(Collectors.toSet()));
            return msgDto;
        }).collect(Collectors.toList());
        collect.sort(Comparator.comparing(MessageDto::getId).reversed());

        PagedListHolder pagedListHolder = new PagedListHolder(collect);
        pagedListHolder.setPageSize(5);
        pagedListHolder.setPage(page);


        return ResponseEntity.ok().body(new MessagesWrapper(principal.getName(), pagedListHolder));
    }


    @PostMapping("/messagePost")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void post(@RequestBody Message msg,Principal principal) {
        msg.setName(msg.getText());
        UserInfo cuurentUser = userInfoService.getUserByName(principal.getName());
        msg.setUser(cuurentUser);
        messageRepo.save(msg);
    }

    @DeleteMapping("/deleteMessage/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public void post(@PathVariable Long id) {
        System.out.println(id+"DELETE");
        messageRepo.deleteById(id);

    }

}
