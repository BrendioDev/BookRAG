package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;

import java.util.Map;

public interface PipelineStage {

    String name();

    void execute(SystemConfig config, Map<String, Object> context);
}
