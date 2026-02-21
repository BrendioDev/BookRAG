package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;

import java.util.Map;

public class KgRefinerStage implements PipelineStage {

    @Override
    public String name() { return "KgRefiner"; }

    @Override
    public void execute(SystemConfig config, Map<String, Object> context) {
        System.out.println("[KgRefiner] Refining knowledge graph, type: " + config.getGraph().getRefineType());
        System.out.println("[KgRefiner] Done (mock).");
    }
}
