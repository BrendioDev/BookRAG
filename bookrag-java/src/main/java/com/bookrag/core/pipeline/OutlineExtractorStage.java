package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;

import java.util.Map;

public class OutlineExtractorStage implements PipelineStage {

    @Override
    public String name() { return "OutlineExtractor"; }

    @Override
    public void execute(SystemConfig config, Map<String, Object> context) {
        System.out.println("[OutlineExtractor] Extracting document outline from: " + config.getPdfPath());
        System.out.println("[OutlineExtractor] Done (mock).");
    }
}
