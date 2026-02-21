package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;

import java.util.Map;

public class KgExtractorStage implements PipelineStage {

    @Override
    public String name() { return "KgExtractor"; }

    @Override
    public void execute(SystemConfig config, Map<String, Object> context) {
        System.out.println("[KgExtractor] Extracting entities using: " + config.getGraph().getExtractorType());
        System.out.println("[KgExtractor] Done (mock).");
    }
}
