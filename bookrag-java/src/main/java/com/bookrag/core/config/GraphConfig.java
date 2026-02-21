package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GraphConfig {

    @JsonProperty("extractor_type")
    private String extractorType = "llm";

    @JsonProperty("local_model_name")
    private String localModelName = "en_core_web_sm";

    @JsonProperty("image_description_force")
    private boolean imageDescriptionForce = false;

    @JsonProperty("max_gleaning")
    private int maxGleaning = 0;

    @JsonProperty("refine_type")
    private String refineType = "advanced";

    @JsonProperty("embedding_config")
    private EmbeddingConfig embeddingConfig = new EmbeddingConfig();

    @JsonProperty("reranker_config")
    private RerankerConfig rerankerConfig = new RerankerConfig();

    public GraphConfig() {}

    public String getExtractorType() { return extractorType; }
    public void setExtractorType(String extractorType) { this.extractorType = extractorType; }
    public String getLocalModelName() { return localModelName; }
    public void setLocalModelName(String localModelName) { this.localModelName = localModelName; }
    public boolean isImageDescriptionForce() { return imageDescriptionForce; }
    public void setImageDescriptionForce(boolean imageDescriptionForce) { this.imageDescriptionForce = imageDescriptionForce; }
    public int getMaxGleaning() { return maxGleaning; }
    public void setMaxGleaning(int maxGleaning) { this.maxGleaning = maxGleaning; }
    public String getRefineType() { return refineType; }
    public void setRefineType(String refineType) { this.refineType = refineType; }
    public EmbeddingConfig getEmbeddingConfig() { return embeddingConfig; }
    public void setEmbeddingConfig(EmbeddingConfig embeddingConfig) { this.embeddingConfig = embeddingConfig; }
    public RerankerConfig getRerankerConfig() { return rerankerConfig; }
    public void setRerankerConfig(RerankerConfig rerankerConfig) { this.rerankerConfig = rerankerConfig; }
}
