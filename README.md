# DevProfile AI — RAG Portfolio Assistant

An AI-powered portfolio application allowing visitors to query professional engineering experience through a conversational interface using Retrieval-Augmented Generation (RAG).

Built with Java 21 and Spring Boot, utilizing Ollama for local model inference and Qdrant Cloud for vector storage.

---

## Tech Stack

- Backend: Java 21, Spring Boot (REST, Spring RestClient)
- Vector Database: Qdrant Cloud (REST API, Cosine distance)
- Embeddings: Ollama (nomic-embed-text, 768 dimensions)
- LLM: Ollama (llama3.2)
- Data Ingestion: Apache Commons CSV
- Frontend: React (Planned)

---

## Project Status

### Current Status (Completed)
- Ingestion Pipeline: Parsed raw CSV logs into 147 day-level document chunks using Apache Commons CSV.
- Embedding Generation: Built swappable `EmbeddingServices` interface and `OllamaEmbeddingService` producing 768-dimensional float vectors.
- Vector Storage: Configured Qdrant Cloud collection (`portfolio-knowledge`) and implemented `QdrantVectorStore` via REST API.
- End-to-End Ingestion: Successfully embedded and upserted documents with payloads into Qdrant Cloud.

### Expected State (When Completed)
- Semantic Retrieval: Vector similarity search retrieving top-K matching logs based on user query intent.
- Context Construction: Dynamic prompt building that feeds relevant historical logs to the LLM.
- Generative Chat Service: Grounded question-answering with fallback behavior for low similarity scores.
- REST API: Expose `/api/chat` endpoints for query handling and streaming responses.
- React Frontend: Interactive chat interface allowing recruiters and visitors to explore work history and technical achievements.

---

## Local Setup

### Prerequisites
- JDK 21
- Ollama running locally with `nomic-embed-text` and `llama3.2`
- Qdrant Cloud cluster and API key

### Configuration (`src/main/resources/application.properties`)
```properties
spring.application.name=devprofile

ollama.base-url=http://localhost:11434
ollama.model.embedding=nomic-embed-text

qdrant.url=https://<your-cluster-url>.cloud.qdrant.io
qdrant.api-key=<your-api-key>
qdrant.collection-name=portfolio-knowledge
