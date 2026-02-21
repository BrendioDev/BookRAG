package com.bookrag.core.provider.mock;

import com.bookrag.core.provider.LlmProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MockLlmProvider implements LlmProvider {

    @Override
    public String getCompletion(String prompt) {
        System.out.println("[MockLLM] getCompletion called, prompt length: " + prompt.length());
        return "Mock LLM response for: " + prompt.substring(0, Math.min(50, prompt.length()));
    }

    @Override
    public Map<String, Object> getJsonCompletion(String prompt) {
        System.out.println("[MockLLM] getJsonCompletion called");
        return Map.of("result", "mock", "prompt_length", prompt.length());
    }

    @Override
    public List<String> batchGetCompletion(List<String> prompts) {
        System.out.println("[MockLLM] batchGetCompletion called, batch size: " + prompts.size());
        return prompts.stream().map(p -> "Mock response").collect(Collectors.toList());
    }
}
