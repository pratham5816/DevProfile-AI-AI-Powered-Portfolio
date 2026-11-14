package com.portfolio.devprofile.dto;

import java.util.List;

public record SearchRequest(List<Float> vector, int limit, boolean with_payload){}
