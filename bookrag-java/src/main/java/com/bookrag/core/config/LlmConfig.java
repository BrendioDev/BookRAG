package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LlmConfig {

    @JsonProperty("model_name")
    private String modelName = "Qwen/Qwen3-8B-AWQ";

    @JsonProperty("api_key")
    private String apiKey = "openai";

    @JsonProperty("api_base")
    private String apiBase = "http://localhost:8003/v1";

    private double temperature = 0.1;

    @JsonProperty("max_tokens")
    private int maxTokens = 5000;

    @JsonProperty("frequency_penalty")
    private double frequencyPenalty = 0.0;

    @JsonProperty("presence_penalty")
    private double presencePenalty = 0.0;

    private String backend = "openai";

    @JsonProperty("max_workers")
    private int maxWorkers = 8;

    public LlmConfig() {}

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getApiBase() { return apiBase; }
    public void setApiBase(String apiBase) { this.apiBase = apiBase; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public int getMaxTokens() { return maxTokens; }
    public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
    public double getFrequencyPenalty() { return frequencyPenalty; }
    public void setFrequencyPenalty(double frequencyPenalty) { this.frequencyPenalty = frequencyPenalty; }
    public double getPresencePenalty() { return presencePenalty; }
    public void setPresencePenalty(double presencePenalty) { this.presencePenalty = presencePenalty; }
    public String getBackend() { return backend; }
    public void setBackend(String backend) { this.backend = backend; }
    public int getMaxWorkers() { return maxWorkers; }
    public void setMaxWorkers(int maxWorkers) { this.maxWorkers = maxWorkers; }
}
