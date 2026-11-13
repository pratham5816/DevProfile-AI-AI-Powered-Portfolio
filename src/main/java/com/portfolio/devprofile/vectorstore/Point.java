package com.portfolio.devprofile.vectorstore;


import java.util.List;
import java.util.Map;

public record Point(String id, List<Float> vector, Map<String, Object> payload) {
}