package com.portfolio.devprofile.rag;

import com.portfolio.devprofile.dto.*;
import com.portfolio.devprofile.embedding.OllamaEmbeddingService;
import com.portfolio.devprofile.vectorstore.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.util.List;

@Service
public class ChatRag {
    private RestClient restClient;
    private String model;
    private OllamaEmbeddingService ollamaEmbeddingService;
    private QdrantVectorStore qdrantVectorStore;
    private int limit;


    public ChatRag(@Value("${ollama.model.chat}") String model,
                   @Value("${ollama.base-url}") String baseUrl,
                   @Value("${qdrant.document.limit}") int limit,
                   @Autowired OllamaEmbeddingService ollamaEmbeddingService,
                   @Autowired QdrantVectorStore qdrantVectorStore) {
         this.restClient = RestClient.builder().baseUrl(baseUrl).build();
         this.model = model;
         this.ollamaEmbeddingService = ollamaEmbeddingService;
         this.qdrantVectorStore = qdrantVectorStore;
         this.limit = limit;
    }


    public ChatResponse chatModel(ChatMessage message , boolean stream) {
        ChatRequest request = new ChatRequest(model , List.of(message) , stream);
       return restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ChatResponse.class);
    }

    public void chatRequest(String userQuery, boolean stream) {
        if(userQuery == null){
            throw new IllegalArgumentException("userQuery cannot be null");
        }
        List<Float> vector = ollamaEmbeddingService.embed(userQuery);
        if(vector == null){
            throw new IllegalArgumentException("problem converting user input");
        }

        SearchResponse response = qdrantVectorStore.searchPoints(vector, limit);

        List<ScoredPoint> points = response.result();
        StringBuilder content =  new StringBuilder(userQuery + "\n\n");
        for(ScoredPoint point : points){
            content.append(point.payload().get("content") + "\n\n");
        }
        System.out.println("------------------------------------------------");
        System.out.println("context for llm with query " + content.toString());
        System.out.println("------------------------------------------------");
    }
}
