package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RerankerConfig {

    @JsonProperty("model_name")
    private String modelName = "Qwen/Qwen3-Reranker-0.6B";

    @JsonProperty("max_length")
    private int maxLength = 8192;

    private String device = "cuda:2";
    private String backend = "local";

    @JsonProperty("api_base")
    private String apiBase = "http://localhost:8011/v1";

    public RerankerConfig() {}

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public int getMaxLength() { return maxLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }
    public String getBackend() { return backend; }
    public void setBackend(String backend) { this.backend = backend; }
    public String getApiBase() { return apiBase; }
    public void setApiBase(String apiBase) { this.apiBase = apiBase; }
}
