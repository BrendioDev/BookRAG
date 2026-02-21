package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatasetConfig {

    @JsonProperty("dataset_path")
    private String datasetPath;

    @JsonProperty("working_dir")
    private String workingDir;

    @JsonProperty("dataset_name")
    private String datasetName;

    public DatasetConfig() {}

    public String getDatasetPath() { return datasetPath; }
    public void setDatasetPath(String datasetPath) { this.datasetPath = datasetPath; }
    public String getWorkingDir() { return workingDir; }
    public void setWorkingDir(String workingDir) { this.workingDir = workingDir; }
    public String getDatasetName() { return datasetName; }
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
}
