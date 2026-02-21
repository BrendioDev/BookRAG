package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VlmConfig {

    private String backend = "ollama";

    @JsonProperty("model_name")
    private String modelName = "qwen2.5vl:6k";

    @JsonProperty("max_tokens")
    private int maxTokens = 6000;

    private double temperature = 0.7;

    @JsonProperty("api_key")
    private String apiKey = "None";

    @JsonProperty("api_base")
    private String apiBase = "http://localhost:11434";

    public VlmConfig() {}

    public String getBackend() { return backend; }
    public void setBackend(String backend) { this.backend = backend; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public int getMaxTokens() { return maxTokens; }
    public void setMaxTokens(int maxTokens) { this.maxTokens = maxTokens; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getApiBase() { return apiBase; }
    public void setApiBase(String apiBase) { this.apiBase = apiBase; }
}
