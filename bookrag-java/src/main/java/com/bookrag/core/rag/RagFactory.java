package com.bookrag.core.rag;

import com.bookrag.core.config.rag.BaseRagStrategyConfig;
import com.bookrag.core.config.rag.GbcRagConfig;
import com.bookrag.core.provider.LlmProvider;
import com.bookrag.core.provider.mock.MockLlmProvider;

public class RagFactory {

    public static BaseRag createRagAgent(BaseRagStrategyConfig strategyConfig) {
        return createRagAgent(strategyConfig, new MockLlmProvider());
    }

    public static BaseRag createRagAgent(BaseRagStrategyConfig strategyConfig, LlmProvider llm) {
        String strategy = strategyConfig.getStrategy();
        System.out.println("[RagFactory] Creating RAG agent with strategy: " + strategy);

        if (strategyConfig instanceof GbcRagConfig) {
            return new GbcRag(llm);
        }

        throw new UnsupportedOperationException("Unknown RAG strategy: " + strategy);
    }
}
