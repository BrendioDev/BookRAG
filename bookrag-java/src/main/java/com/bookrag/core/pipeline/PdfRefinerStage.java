package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;

import java.util.Map;

public class PdfRefinerStage implements PipelineStage {

    @Override
    public String name() { return "PdfRefiner"; }

    @Override
    public void execute(SystemConfig config, Map<String, Object> context) {
        System.out.println("[PdfRefiner] Refining PDF content from: " + config.getPdfPath());
        System.out.println("[PdfRefiner] Done (mock).");
    }
}
