package com.portfolio.devprofile.embedding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OllamaEmbeddingService implements EmbeddingServices{

    private final String model;
    private final RestClient restClient;
    record OllamaEmbeddingRequest(String model, String prompt) {}
    record OllamaEmbeddingResponse(List<Float> embedding) {}

    public OllamaEmbeddingService(@Value("${ollama.base-url}") String baseUrl , @Value("${ollama.model.embedding}") String model){
        this.model = model;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public List<Float> embed(String text) {
        OllamaEmbeddingRequest request = new OllamaEmbeddingRequest(model, text);
            OllamaEmbeddingResponse response = restClient.post()
                    .uri("api/embeddings")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(OllamaEmbeddingResponse.class);

            if(response == null || response.embedding().size() != 768){
                throw new RuntimeException("ollama integration layer is broken");
            }
            return response.embedding();
    }

}
