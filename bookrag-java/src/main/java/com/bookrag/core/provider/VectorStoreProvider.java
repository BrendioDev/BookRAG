package com.bookrag.core.provider;

import java.util.List;
import java.util.Map;

public interface VectorStoreProvider {

    List<String> addTexts(List<String> texts, List<Map<String, String>> metadatas);

    List<Map<String, Object>> search(String queryText, int topK);

    void reset();
}
