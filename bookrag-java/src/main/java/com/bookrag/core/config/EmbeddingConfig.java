package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddingConfig {

    private String type = "text";
    private String backend = "local";

    @JsonProperty("api_key")
    private String apiKey = "ollama";

    @JsonProperty("api_base")
    private String apiBase = "http://localhost:11434";

    @JsonProperty("model_name")
    private String modelName = "Qwen/Qwen3-Embedding-0.6B";

    @JsonProperty("max_length")
    private int maxLength = 8192;

    private String device = "cuda:2";

    public EmbeddingConfig() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getBackend() { return backend; }
    public void setBackend(String backend) { this.backend = backend; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getApiBase() { return apiBase; }
    public void setApiBase(String apiBase) { this.apiBase = apiBase; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public int getMaxLength() { return maxLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }
}
