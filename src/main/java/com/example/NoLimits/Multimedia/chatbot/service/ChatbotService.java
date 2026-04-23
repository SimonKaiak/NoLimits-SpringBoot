package com.example.NoLimits.Multimedia.chatbot.service;

import com.example.NoLimits.Multimedia.chatbot.dto.ChatResponse;

public interface ChatbotService {
    ChatResponse getWelcomeMessage();
    ChatResponse processMessage(String message);
}