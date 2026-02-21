package com.bookrag.core.config.rag;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = GbcRagConfig.class, name = "gbc")
})
public abstract class BaseRagStrategyConfig {

    public abstract String getStrategy();
}
