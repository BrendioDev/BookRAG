package com.bookrag.core.rag;

import com.bookrag.core.provider.LlmProvider;

import java.util.List;

public abstract class BaseRag {

    protected LlmProvider llm;
    protected String name;
    protected String description;

    protected BaseRag(LlmProvider llm, String name, String description) {
        this.llm = llm;
        this.name = name;
        this.description = description;
    }

    public abstract Object retrieve(String query);

    public abstract String createAugmentedPrompt(String query);

    public abstract RagResult generation(String query, String queryOutputDir);

    public abstract void close();

    public String getName() { return name; }
    public String getDescription() { return description; }

    public static class RagResult {
        private String answer;
        private List<Integer> retrievedNodeIds;

        public RagResult(String answer, List<Integer> retrievedNodeIds) {
            this.answer = answer;
            this.retrievedNodeIds = retrievedNodeIds;
        }

        public String getAnswer() { return answer; }
        public List<Integer> getRetrievedNodeIds() { return retrievedNodeIds; }
    }
}
