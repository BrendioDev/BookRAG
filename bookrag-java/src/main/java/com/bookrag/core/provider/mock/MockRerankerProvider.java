package com.bookrag.core.provider.mock;

import com.bookrag.core.provider.RerankerProvider;

import java.util.ArrayList;
import java.util.List;

public class MockRerankerProvider implements RerankerProvider {

    @Override
    public List<Double> rerank(String query, List<String> documents) {
        System.out.println("[MockReranker] rerank called, docs: " + documents.size());
        List<Double> scores = new ArrayList<>();
        for (int i = 0; i < documents.size(); i++) {
            scores.add(1.0 / (i + 1));
        }
        return scores;
    }

    @Override
    public void close() {
        System.out.println("[MockReranker] closed");
    }
}
