package com.portfolio.devprofile.embedding;

import java.util.List;

public interface EmbeddingServices {
    List<Float> embed(String text);
}
