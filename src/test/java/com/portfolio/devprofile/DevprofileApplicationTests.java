package com.portfolio.devprofile;

import com.portfolio.devprofile.embedding.OllamaEmbeddingService;
import com.portfolio.devprofile.ingestion.Document;
import com.portfolio.devprofile.ingestion.IngestionService;
import com.portfolio.devprofile.ingestion.Parser;
import com.portfolio.devprofile.vectorstore.QdrantVectorStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DevprofileApplicationTests {

    @Autowired
    Parser parser;
    @Autowired
    QdrantVectorStore qdrantVectorStore;
    @Autowired
    IngestionService ingestionService;

	@Test
	void contextLoads() {
	}

    @Test
    @DisplayName("Test parser with actual work_portfolio.csv file")
    void testParserWithActualFile() throws Exception {
        try (InputStream stream = getClass().getResourceAsStream("/data/work_portfolio.csv")) {
            // Check file exists
            assertNotNull(stream, "work_portfolio.csv should exist in resources");
            List<Document> docs = parser.parseDocuments(stream);
            System.out.println("Total documents parsed: " + docs.size());
            assertFalse(docs.isEmpty(), "Documents list should not be empty");

            // Print first document to inspect output
            System.out.println("Sample Document:\n" + docs.get(0).content());
            for(Document doc : docs) {;
                 System.out.println("-------------------------------------------------------------------------------");
                 System.out.println(doc);
                 System.out.println("--------------------------------------------------------------------------------");
            }
        }
    }

    @Test
    @DisplayName("Test ollama api for embedding generation")
    void testOllamaApiLocal() {
        OllamaEmbeddingService embeddingService = new OllamaEmbeddingService("http://localhost:11434", "nomic-embed-text");
        List<Float> resp = embeddingService.embed("What pratham did in shopify integration");
        System.out.println("Embedding: " + resp);
        System.out.println("Embedding size: " + resp.size());
    }

    @Test
    @DisplayName("Test qdrant collections api")
    void testQdrantCollectionsApi() {
        ResponseEntity<String> response = qdrantVectorStore.checkCollection();
        System.out.println(response.getBody());
    }

    @Test
    @DisplayName("Test document to point conversion function")
    void testDocumentToPointConversionFunction() {
//        ingestionService.runIngestion();
    }


}
