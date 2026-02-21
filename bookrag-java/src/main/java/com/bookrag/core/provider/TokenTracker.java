package com.bookrag.core.provider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TokenTracker {

    private static volatile TokenTracker instance;

    private final AtomicLong promptTokens = new AtomicLong(0);
    private final AtomicLong completionTokens = new AtomicLong(0);
    private final AtomicLong totalTokens = new AtomicLong(0);
    private final Map<String, Map<String, Long>> stageHistory = new LinkedHashMap<>();
    private long lastStagePromptTokens = 0;
    private long lastStageCompletionTokens = 0;

    private TokenTracker() {}

    public static TokenTracker getInstance() {
        if (instance == null) {
            synchronized (TokenTracker.class) {
                if (instance == null) {
                    instance = new TokenTracker();
                }
            }
        }
        return instance;
    }

    public synchronized void addUsage(long prompt, long completion) {
        promptTokens.addAndGet(prompt);
        completionTokens.addAndGet(completion);
        totalTokens.addAndGet(prompt + completion);
    }

    public synchronized Map<String, Long> getUsage() {
        return Map.of(
            "prompt_tokens", promptTokens.get(),
            "completion_tokens", completionTokens.get(),
            "total_tokens", totalTokens.get()
        );
    }

    public synchronized void reset() {
        promptTokens.set(0);
        completionTokens.set(0);
        totalTokens.set(0);
        stageHistory.clear();
        lastStagePromptTokens = 0;
        lastStageCompletionTokens = 0;
    }

    public synchronized Map<String, Long> recordStage(String stageName) {
        long stagePrompt = promptTokens.get() - lastStagePromptTokens;
        long stageCompletion = completionTokens.get() - lastStageCompletionTokens;
        long stageTotal = stagePrompt + stageCompletion;

        Map<String, Long> stageUsage = Map.of(
            "prompt_tokens", stagePrompt,
            "completion_tokens", stageCompletion,
            "total_tokens", stageTotal
        );
        stageHistory.put(stageName, stageUsage);
        lastStagePromptTokens = promptTokens.get();
        lastStageCompletionTokens = completionTokens.get();
        return stageUsage;
    }

    public Map<String, Map<String, Long>> getStageHistory() {
        return stageHistory;
    }

    @Override
    public String toString() {
        Map<String, Long> usage = getUsage();
        return "Token Usage | Prompt: " + usage.get("prompt_tokens")
                + " | Completion: " + usage.get("completion_tokens")
                + " | Total: " + usage.get("total_tokens");
    }
}
