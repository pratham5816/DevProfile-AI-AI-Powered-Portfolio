package com.portfolio.devprofile.ingestion;

import com.portfolio.devprofile.embedding.OllamaEmbeddingService;
import com.portfolio.devprofile.vectorstore.Point;
import com.portfolio.devprofile.vectorstore.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IngestionService {

    private final Parser parser;
    private final OllamaEmbeddingService ollamaEmbeddingService;
    private final QdrantVectorStore qdrantVectorStore;

    public IngestionService(@Autowired Parser parser, @Autowired OllamaEmbeddingService ollamaEmbeddingService, @Autowired QdrantVectorStore qdrantVectorStore) {
        this.parser = parser;
        this.ollamaEmbeddingService = ollamaEmbeddingService;
        this.qdrantVectorStore = qdrantVectorStore;
    }
    public void runIngestion(){
        try{
            InputStream stream = getClass().getResourceAsStream("/data/work_portfolio.csv");
            List<Document> documents = parser.parseDocuments(stream);
            List<Point> points = new ArrayList<>();
            for(Document document : documents){
                Map<String, Object> payload = Map.of("content" , document.content() , "title" , document.title());
                List<Float> list = ollamaEmbeddingService.embed(document.content());
                points.add(new Point(document.id(), list , payload));
            }
            qdrantVectorStore.upsertPoints(points);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
