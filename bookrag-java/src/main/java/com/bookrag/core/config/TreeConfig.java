package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TreeConfig {

    @JsonProperty("node_keywords")
    private boolean nodeKeywords = true;

    @JsonProperty("node_summary")
    private boolean nodeSummary = false;

    @JsonProperty("use_vlm")
    private boolean useVlm = false;

    public TreeConfig() {}

    public boolean isNodeKeywords() { return nodeKeywords; }
    public void setNodeKeywords(boolean nodeKeywords) { this.nodeKeywords = nodeKeywords; }
    public boolean isNodeSummary() { return nodeSummary; }
    public void setNodeSummary(boolean nodeSummary) { this.nodeSummary = nodeSummary; }
    public boolean isUseVlm() { return useVlm; }
    public void setUseVlm(boolean useVlm) { this.useVlm = useVlm; }
}
