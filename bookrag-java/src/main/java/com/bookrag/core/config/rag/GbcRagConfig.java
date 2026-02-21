package com.bookrag.core.config.rag;

import com.bookrag.core.config.EmbeddingConfig;
import com.bookrag.core.config.RerankerConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GbcRagConfig extends BaseRagStrategyConfig {

    private String strategy = "gbc";

    // Note: preserving the Python typo "varient" for config compatibility
    private String varient = "standard";

    private int topk = 10;

    @JsonProperty("sim_threshold_e")
    private double simThresholdE = 0.3;

    @JsonProperty("select_depth")
    private int selectDepth = 2;

    @JsonProperty("x_percentile")
    private double xPercentile = 0.85;

    private double alpha = 0.5;

    @JsonProperty("topk_ent")
    private int topkEnt = 5;

    @JsonProperty("max_retry")
    private int maxRetry = 3;

    @JsonProperty("reranker_config")
    private RerankerConfig rerankerConfig = new RerankerConfig();

    @JsonProperty("mm_reranker_config")
    private EmbeddingConfig mmRerankerConfig = new EmbeddingConfig();

    public GbcRagConfig() {}

    @Override
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public String getVarient() { return varient; }
    public void setVarient(String varient) { this.varient = varient; }
    public int getTopk() { return topk; }
    public void setTopk(int topk) { this.topk = topk; }
    public double getSimThresholdE() { return simThresholdE; }
    public void setSimThresholdE(double simThresholdE) { this.simThresholdE = simThresholdE; }
    public int getSelectDepth() { return selectDepth; }
    public void setSelectDepth(int selectDepth) { this.selectDepth = selectDepth; }
    public double getxPercentile() { return xPercentile; }
    public void setxPercentile(double xPercentile) { this.xPercentile = xPercentile; }
    public double getAlpha() { return alpha; }
    public void setAlpha(double alpha) { this.alpha = alpha; }
    public int getTopkEnt() { return topkEnt; }
    public void setTopkEnt(int topkEnt) { this.topkEnt = topkEnt; }
    public int getMaxRetry() { return maxRetry; }
    public void setMaxRetry(int maxRetry) { this.maxRetry = maxRetry; }
    public RerankerConfig getRerankerConfig() { return rerankerConfig; }
    public void setRerankerConfig(RerankerConfig rerankerConfig) { this.rerankerConfig = rerankerConfig; }
    public EmbeddingConfig getMmRerankerConfig() { return mmRerankerConfig; }
    public void setMmRerankerConfig(EmbeddingConfig mmRerankerConfig) { this.mmRerankerConfig = mmRerankerConfig; }
}
