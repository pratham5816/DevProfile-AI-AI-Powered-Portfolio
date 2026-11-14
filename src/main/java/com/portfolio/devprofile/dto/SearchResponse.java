package com.portfolio.devprofile.dto;

import java.util.List;

public record SearchResponse(List<ScoredPoint> result, String status) {}

