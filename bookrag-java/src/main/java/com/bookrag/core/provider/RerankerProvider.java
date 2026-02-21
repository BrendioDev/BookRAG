package com.bookrag.core.provider;

import java.util.List;

public interface RerankerProvider {

    List<Double> rerank(String query, List<String> documents);

    void close();
}
