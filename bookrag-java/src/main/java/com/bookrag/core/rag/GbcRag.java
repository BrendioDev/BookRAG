package com.bookrag.core.rag;

import com.bookrag.core.provider.LlmProvider;

import java.util.Collections;
import java.util.List;

public class GbcRag extends BaseRag {

    public GbcRag(LlmProvider llm) {
        super(llm, "GBC RAG", "Graph-Based Contextual RAG");
    }

    @Override
    public Object retrieve(String query) {
        System.out.println("[GbcRag] Retrieving for query: " + query);
        return Collections.emptyList();
    }

    @Override
    public String createAugmentedPrompt(String query) {
        return "[Mock augmented prompt for: " + query + "]";
    }

    @Override
    public RagResult generation(String query, String queryOutputDir) {
        System.out.println("[GbcRag] Generating answer for: " + query);
        System.out.println("[GbcRag] Output dir: " + queryOutputDir);
        return new RagResult("Mock answer for: " + query, List.of(1, 2, 3));
    }

    @Override
    public void close() {
        System.out.println("[GbcRag] Closed.");
    }
}
