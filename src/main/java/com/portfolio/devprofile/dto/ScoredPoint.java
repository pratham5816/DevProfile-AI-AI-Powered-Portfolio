package com.portfolio.devprofile.dto;

import java.util.Map;

public record ScoredPoint(String id, Float score, Map<String, Object> payload) {}
