package com.portfolio.devprofile.dto;

import java.util.List;

public record ChatRequest(String model, List<ChatMessage> messages, boolean stream) {}

