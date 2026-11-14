package com.portfolio.devprofile.dto;

import java.util.Map;

public record ChatResponse(Map<String, Object> message, String done) {}

