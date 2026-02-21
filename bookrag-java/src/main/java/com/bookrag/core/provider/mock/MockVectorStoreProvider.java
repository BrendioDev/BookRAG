package com.bookrag.core.provider.mock;

import com.bookrag.core.provider.VectorStoreProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MockVectorStoreProvider implements VectorStoreProvider {

    @Override
    public List<String> addTexts(List<String> texts, List<Map<String, String>> metadatas) {
        System.out.println("[MockVDB] addTexts called, count: " + texts.size());
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            ids.add("mock-id-" + i);
        }
        return ids;
    }

    @Override
    public List<Map<String, Object>> search(String queryText, int topK) {
        System.out.println("[MockVDB] search called, query: " + queryText.substring(0, Math.min(30, queryText.length())));
        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, 3); i++) {
            results.add(Map.of("id", "mock-" + i, "score", 0.9 - i * 0.1, "text", "Mock result " + i));
        }
        return results;
    }

    @Override
    public void reset() {
        System.out.println("[MockVDB] reset");
    }
}
