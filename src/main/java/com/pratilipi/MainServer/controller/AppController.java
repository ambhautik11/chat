package com.pratilipi.MainServer.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.pratilipi.MainServer.Service.MessageService;
import com.pratilipi.MainServer.Service.UserService;
import com.pratilipi.MainServer.model.Message;
import com.pratilipi.MainServer.model.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AppController {

    private StompEndpointRegistry stompEndpointRegistry;
    private AmazonDynamoDB amazonDynamoDB;
    private SimpMessageSendingOperations simpMessagingTemplate;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    AppController(@Autowired AmazonDynamoDB amazonDynamoDB, @Autowired SimpMessageSendingOperations simpMessageSendingOperations) {
//        this.stompEndpointRegistry = stompEndpointRegistry;
        this.simpMessagingTemplate = simpMessageSendingOperations;
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @PostMapping("/login")
    @CrossOrigin(origins="*", maxAge=3600)
    private boolean login(@RequestBody User user) {
        return userService.logIn(user);
//        stompEndpointRegistry.addEndpoint(user.getUser_id()+"/").setAllowedOrigins("*").withSockJS();
    }

    @MessageMapping("/chat")
    private Message postMessage(@RequestBody Message message) {
//        Message message = new Message();
        String topic = "/messages/";
        topic += message.getTo_user();
        messageService.save(message);
        System.out.println("Routed " + message + " to " + topic);
        this.simpMessagingTemplate.convertAndSend(topic, message);
        return message;
    }

    @PostMapping("/message")
    private Message postMessag1e(@RequestBody Message message) {
       messageService.save(message);
        return message;
    }

    @PostMapping("/messages")
    @CrossOrigin("*")
    private List<Message> getMessages(@RequestBody User user){
        return messageService.getAllMessages(user);
    }

}
