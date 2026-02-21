package com.bookrag.core.provider.mock;

import com.bookrag.core.provider.EmbeddingProvider;

import java.util.List;

public class MockEmbeddingProvider implements EmbeddingProvider {

    @Override
    public double[][] embedTexts(List<String> texts) {
        System.out.println("[MockEmbedding] embedTexts called, count: " + texts.size());
        double[][] embeddings = new double[texts.size()][384];
        for (int i = 0; i < texts.size(); i++) {
            embeddings[i][0] = i * 0.1;
        }
        return embeddings;
    }

    @Override
    public void close() {
        System.out.println("[MockEmbedding] closed");
    }
}
