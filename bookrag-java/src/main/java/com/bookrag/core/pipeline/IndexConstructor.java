package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexConstructor {

    private static final Logger log = LoggerFactory.getLogger(IndexConstructor.class);

    private final List<PipelineStage> treeStages = List.of(
            new PdfRefinerStage(),
            new OutlineExtractorStage(),
            new DocTreeBuilderStage()
    );

    private final List<PipelineStage> graphStages = List.of(
            new KgExtractorStage(),
            new KgBuilderStage(),
            new KgRefinerStage()
    );

    private final PipelineStage vdbStage = new VdbIndexStage();

    public void buildIndex(SystemConfig config, String stage) {
        log.info("Building index, stage: {}, pdf: {}", stage, config.getPdfPath());

        Map<String, Object> context = new HashMap<>();

        switch (stage) {
            case "tree":
                runTreeStages(config, context);
                break;
            case "graph":
                runGraphStages(config, context);
                break;
            case "vdb":
                runVdbStage(config, context);
                break;
            case "all":
                runTreeStages(config, context);
                runGraphStages(config, context);
                runVdbStage(config, context);
                break;
            case "rebuild_graph_vdb":
                log.info("[IndexConstructor] Rebuilding graph VDB (mock)");
                break;
            default:
                throw new IllegalArgumentException("Unknown stage: " + stage);
        }

        log.info("Index construction complete for stage: {}", stage);
    }

    private void runTreeStages(SystemConfig config, Map<String, Object> context) {
        for (PipelineStage stage : treeStages) {
            log.info("Running stage: {}", stage.name());
            stage.execute(config, context);
        }
    }

    private void runGraphStages(SystemConfig config, Map<String, Object> context) {
        for (PipelineStage stage : graphStages) {
            log.info("Running stage: {}", stage.name());
            stage.execute(config, context);
        }
    }

    private void runVdbStage(SystemConfig config, Map<String, Object> context) {
        log.info("Running stage: {}", vdbStage.name());
        vdbStage.execute(config, context);
    }
}
