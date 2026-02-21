package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;

import java.util.Map;

public class KgBuilderStage implements PipelineStage {

    @Override
    public String name() { return "KgBuilder"; }

    @Override
    public void execute(SystemConfig config, Map<String, Object> context) {
        System.out.println("[KgBuilder] Building knowledge graph");
        System.out.println("[KgBuilder] Done (mock).");
    }
}
