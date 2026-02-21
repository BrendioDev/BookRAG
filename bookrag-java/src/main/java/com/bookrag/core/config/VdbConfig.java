package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VdbConfig {

    @JsonProperty("mm_embedding")
    private boolean mmEmbedding = true;

    @JsonProperty("vdb_dir_name")
    private String vdbDirName = "./chroma_db";

    @JsonProperty("collection_name")
    private String collectionName = "default_collection";

    @JsonProperty("embedding_config")
    private EmbeddingConfig embeddingConfig = new EmbeddingConfig();

    @JsonProperty("force_rebuild")
    private boolean forceRebuild = true;

    public VdbConfig() {}

    public boolean isMmEmbedding() { return mmEmbedding; }
    public void setMmEmbedding(boolean mmEmbedding) { this.mmEmbedding = mmEmbedding; }
    public String getVdbDirName() { return vdbDirName; }
    public void setVdbDirName(String vdbDirName) { this.vdbDirName = vdbDirName; }
    public String getCollectionName() { return collectionName; }
    public void setCollectionName(String collectionName) { this.collectionName = collectionName; }
    public EmbeddingConfig getEmbeddingConfig() { return embeddingConfig; }
    public void setEmbeddingConfig(EmbeddingConfig embeddingConfig) { this.embeddingConfig = embeddingConfig; }
    public boolean isForceRebuild() { return forceRebuild; }
    public void setForceRebuild(boolean forceRebuild) { this.forceRebuild = forceRebuild; }
}
