package com.buchs.fullstack.service;

import com.buchs.fullstack.entity.Message;
import com.buchs.fullstack.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    public void saveMessage(Message msg) {
        messageRepo.save(msg);
    }


    public void deleteMessageById(Long id) {
        messageRepo.deleteById(id);
    }


    public void getAllMessages() {}


    public Message getMessageById(Long id) {
        return messageRepo.findById(id).orElseThrow();
    }


}
