package com.bookrag.core.index;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Graph {

    private static final Logger log = LoggerFactory.getLogger(Graph.class);

    private org.jgrapht.Graph<String, DefaultEdge> kg;
    private Map<String, Entity> nodeEntities = new HashMap<>();
    private Map<String, Relationship> edgeRelationships = new HashMap<>();
    private Map<Integer, Set<String>> tree2kg = new HashMap<>();
    private String saveDir;
    private String variant;

    public Graph(String savePath, String variant) {
        this.kg = new SimpleGraph<>(DefaultEdge.class);
        this.saveDir = savePath;
        this.variant = variant;
    }

    public Set<String> getAllNodes() {
        return kg.vertexSet();
    }

    public String getNodeNameFromEntity(Entity entity) {
        return getNodeNameFromStr(entity.getEntityName(), entity.getEntityType());
    }

    public String getNodeNameFromStr(String entityName, String entityType) {
        return "Name: " + entityName + "\nType: " + entityType;
    }

    public void addKgNode(Entity entity) {
        String nodeName = getNodeNameFromEntity(entity);
        kg.addVertex(nodeName);
        nodeEntities.put(nodeName, entity);
    }

    public void addKgEdge(Relationship rel, String srcType, String tgtType) {
        String srcNodeName = getNodeNameFromStr(rel.getSrcEntityName(), srcType);
        String tgtNodeName = getNodeNameFromStr(rel.getTgtEntityName(), tgtType);
        if (!kg.containsVertex(srcNodeName)) {
            throw new IllegalArgumentException("Source node '" + srcNodeName + "' not found in knowledge graph.");
        }
        if (!kg.containsVertex(tgtNodeName)) {
            throw new IllegalArgumentException("Target node '" + tgtNodeName + "' not found in knowledge graph.");
        }
        kg.addEdge(srcNodeName, tgtNodeName);
        edgeRelationships.put(srcNodeName + "||" + tgtNodeName, rel);
    }

    public void link(int treeNodeId, String entityName, String entityType) {
        String nodeName = getNodeNameFromStr(entityName, entityType);
        if (!kg.containsVertex(nodeName)) {
            throw new IllegalArgumentException("KG node '" + nodeName + "' not found in knowledge graph.");
        }
        tree2kg.computeIfAbsent(treeNodeId, k -> new HashSet<>()).add(nodeName);
    }

    public void addAndLink(int treeNodeId, List<Entity> entities) {
        for (Entity entity : entities) {
            String nodeName = getNodeNameFromEntity(entity);
            if (!kg.containsVertex(nodeName)) {
                addKgNode(entity);
            }
            link(treeNodeId, entity.getEntityName(), entity.getEntityType());
        }
    }

    public Entity getEntity(String entityName, String entityType) {
        String nodeName = getNodeNameFromStr(entityName, entityType);
        Entity entity = nodeEntities.get(nodeName);
        if (entity == null) {
            throw new IllegalArgumentException("Entity '" + nodeName + "' not found in knowledge graph.");
        }
        return entity;
    }

    public Entity getEntityByNodeName(String nodeName) {
        Entity entity = nodeEntities.get(nodeName);
        if (entity == null) {
            throw new IllegalArgumentException("Node '" + nodeName + "' not found in knowledge graph.");
        }
        return entity;
    }

    public Map<Integer, Set<String>> getTree2kg() { return tree2kg; }
    public String getSaveDir() { return saveDir; }
    public String getVariant() { return variant; }
    public int getNodeCount() { return kg.vertexSet().size(); }
    public int getEdgeCount() { return kg.edgeSet().size(); }
}
