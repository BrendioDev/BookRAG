package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemConfig {

    private LlmConfig llm = new LlmConfig();
    private VlmConfig vlm = new VlmConfig();
    private MineruConfig mineru = new MineruConfig();
    private TreeConfig tree = new TreeConfig();
    private GraphConfig graph = new GraphConfig();
    private VdbConfig vdb = new VdbConfig();

    @JsonProperty("index_type")
    private String indexType = "gbc";

    @JsonProperty("rag_force_reprocess")
    private boolean ragForceReprocess = false;

    private RagConfig rag = new RagConfig();

    @JsonProperty("pdf_path")
    private String pdfPath;

    @JsonProperty("save_path")
    private String savePath;

    public SystemConfig() {}

    public LlmConfig getLlm() { return llm; }
    public void setLlm(LlmConfig llm) { this.llm = llm; }
    public VlmConfig getVlm() { return vlm; }
    public void setVlm(VlmConfig vlm) { this.vlm = vlm; }
    public MineruConfig getMineru() { return mineru; }
    public void setMineru(MineruConfig mineru) { this.mineru = mineru; }
    public TreeConfig getTree() { return tree; }
    public void setTree(TreeConfig tree) { this.tree = tree; }
    public GraphConfig getGraph() { return graph; }
    public void setGraph(GraphConfig graph) { this.graph = graph; }
    public VdbConfig getVdb() { return vdb; }
    public void setVdb(VdbConfig vdb) { this.vdb = vdb; }
    public String getIndexType() { return indexType; }
    public void setIndexType(String indexType) { this.indexType = indexType; }
    public boolean isRagForceReprocess() { return ragForceReprocess; }
    public void setRagForceReprocess(boolean ragForceReprocess) { this.ragForceReprocess = ragForceReprocess; }
    public RagConfig getRag() { return rag; }
    public void setRag(RagConfig rag) { this.rag = rag; }
    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
    public String getSavePath() { return savePath; }
    public void setSavePath(String savePath) { this.savePath = savePath; }
}
