package com.bookrag.core.config;

import com.bookrag.core.config.rag.BaseRagStrategyConfig;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RagConfig {

    @JsonProperty("strategy_config")
    private BaseRagStrategyConfig strategyConfig;

    public RagConfig() {}

    public BaseRagStrategyConfig getStrategyConfig() { return strategyConfig; }
    public void setStrategyConfig(BaseRagStrategyConfig strategyConfig) { this.strategyConfig = strategyConfig; }
}
