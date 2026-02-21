package com.bookrag.core.index;

import java.util.HashSet;
import java.util.Set;

public class Relationship {

    private String srcEntityName;
    private String tgtEntityName;
    private String relationName;
    private double weight;
    private String description;
    private Set<Integer> sourceIds;

    public Relationship(String srcEntityName, String tgtEntityName, String relationName,
                        double weight, String description, Set<Integer> sourceIds) {
        this.srcEntityName = srcEntityName;
        this.tgtEntityName = tgtEntityName;
        this.relationName = relationName != null ? relationName : "";
        this.weight = weight;
        this.description = description != null ? description : "";
        this.sourceIds = sourceIds != null ? sourceIds : new HashSet<>();
    }

    public Relationship(String srcEntityName, String tgtEntityName, String relationName) {
        this(srcEntityName, tgtEntityName, relationName, 0.0, "", new HashSet<>());
    }

    public String getSrcEntityName() { return srcEntityName; }
    public void setSrcEntityName(String srcEntityName) { this.srcEntityName = srcEntityName; }
    public String getTgtEntityName() { return tgtEntityName; }
    public void setTgtEntityName(String tgtEntityName) { this.tgtEntityName = tgtEntityName; }
    public String getRelationName() { return relationName; }
    public void setRelationName(String relationName) { this.relationName = relationName; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Integer> getSourceIds() { return sourceIds; }
    public void setSourceIds(Set<Integer> sourceIds) { this.sourceIds = sourceIds; }
}
