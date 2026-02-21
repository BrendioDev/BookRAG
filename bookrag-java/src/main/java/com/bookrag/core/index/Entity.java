package com.bookrag.core.index;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Entity {

    private String entityName;
    private String entityType;
    private String description;
    private Set<Integer> sourceIds;

    public Entity(String entityName, String entityType, String description, Set<Integer> sourceIds) {
        this.entityName = entityName;
        this.entityType = entityType != null ? entityType : "";
        this.description = description != null ? description : "";
        this.sourceIds = sourceIds != null ? sourceIds : new HashSet<>();
    }

    public Entity(String entityName, String entityType) {
        this(entityName, entityType, "", new HashSet<>());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityName, entityType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity other)) return false;
        return Objects.equals(entityName, other.entityName)
                && Objects.equals(entityType, other.entityType);
    }

    @Override
    public String toString() {
        return "Entity{name='" + entityName + "', type='" + entityType + "'}";
    }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Integer> getSourceIds() { return sourceIds; }
    public void setSourceIds(Set<Integer> sourceIds) { this.sourceIds = sourceIds; }
}
