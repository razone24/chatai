package com.devtalks.demo.ai.chat.controller;

import com.devtalks.demo.ai.chat.service.ChatAIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatAIController {

    private final ChatAIService chatAIService;

    public ChatAIController(ChatAIService chatAIService) {
        this.chatAIService = chatAIService;
    }


    @GetMapping("/{user}/ask")
    String ask(@PathVariable String user, @RequestParam String message) {
        return chatAIService.ask(user, message);
    }
}
