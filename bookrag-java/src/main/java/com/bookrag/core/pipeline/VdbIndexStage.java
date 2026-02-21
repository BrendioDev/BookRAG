package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;

import java.util.Map;

public class VdbIndexStage implements PipelineStage {

    @Override
    public String name() { return "VdbIndex"; }

    @Override
    public void execute(SystemConfig config, Map<String, Object> context) {
        System.out.println("[VdbIndex] Building vector database: " + config.getVdb().getCollectionName());
        System.out.println("[VdbIndex] Done (mock).");
    }
}
