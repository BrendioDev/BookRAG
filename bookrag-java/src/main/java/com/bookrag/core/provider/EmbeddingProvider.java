package com.bookrag.core.provider;

import java.util.List;

public interface EmbeddingProvider {

    double[][] embedTexts(List<String> texts);

    void close();
}
