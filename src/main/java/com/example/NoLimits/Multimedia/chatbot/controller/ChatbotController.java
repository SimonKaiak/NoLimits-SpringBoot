package com.example.NoLimits.Multimedia.chatbot.controller;

import com.example.NoLimits.Multimedia.chatbot.dto.ChatRequest;
import com.example.NoLimits.Multimedia.chatbot.dto.ChatResponse;
import com.example.NoLimits.Multimedia.chatbot.service.ChatbotService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @GetMapping("/welcome")
    public ChatResponse welcome() {
        return chatbotService.getWelcomeMessage();
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatbotService.processMessage(request.getMessage());
    }
}