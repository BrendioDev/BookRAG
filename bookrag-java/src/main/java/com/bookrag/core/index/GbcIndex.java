package com.bookrag.core.index;

import com.bookrag.core.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GbcIndex {

    private static final Logger log = LoggerFactory.getLogger(GbcIndex.class);

    private String saveDir;
    private SystemConfig config;
    private DocumentTree treeIndex;
    private Graph graphIndex;

    public GbcIndex(SystemConfig config, Graph graphIndex, DocumentTree treeIndex) {
        this.saveDir = config.getSavePath();
        this.config = config;
        this.treeIndex = treeIndex;
        this.graphIndex = graphIndex;
        log.info("GbcIndex initialized at {}", saveDir);
    }

    public void saveGbcIndex() {
        log.info("[GbcIndex] Saving GBC index (mock)");
        if (treeIndex != null) {
            log.info("[GbcIndex] Tree index would be saved");
        }
        if (graphIndex != null) {
            log.info("[GbcIndex] Graph index would be saved");
        }
    }

    public static GbcIndex loadGbcIndex(SystemConfig config) {
        log.info("[GbcIndex] Loading GBC index from {} (mock)", config.getSavePath());
        return new GbcIndex(config, null, null);
    }

    public String getSaveDir() { return saveDir; }
    public SystemConfig getConfig() { return config; }
    public DocumentTree getTreeIndex() { return treeIndex; }
    public void setTreeIndex(DocumentTree treeIndex) { this.treeIndex = treeIndex; }
    public Graph getGraphIndex() { return graphIndex; }
    public void setGraphIndex(Graph graphIndex) { this.graphIndex = graphIndex; }
}
