package com.bookrag.core.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeNode {

    private List<TreeNode> children = new ArrayList<>();
    private TreeNode parent;
    private NodeType type;
    private MetaInfo metaInfo;
    private int depth = 0;
    private int indexId = -1;
    private boolean outlineNode = false;
    private String summary = "";

    public TreeNode(Map<String, Object> metaDict) {
        this.metaInfo = new MetaInfo(metaDict);
    }

    public void addChild(TreeNode child) {
        child.parent = this;
        child.depth = this.depth + 1;
        this.children.add(child);
    }

    public List<int[]> getOutlineEntries() {
        List<int[]> entries = new ArrayList<>();
        if (!outlineNode) return entries;

        String title = metaInfo.getContent() != null ? metaInfo.getContent() : "Untitled";
        // Store as [depth, indexId] — title stored separately
        entries.add(new int[]{depth, indexId});

        for (TreeNode child : children) {
            entries.addAll(child.getOutlineEntries());
        }
        return entries;
    }

    public List<TreeNode> getChildren() { return children; }
    public TreeNode getParent() { return parent; }
    public void setParent(TreeNode parent) { this.parent = parent; }
    public NodeType getType() { return type; }
    public void setType(NodeType type) { this.type = type; }
    public MetaInfo getMetaInfo() { return metaInfo; }
    public void setMetaInfo(MetaInfo metaInfo) { this.metaInfo = metaInfo; }
    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }
    public int getIndexId() { return indexId; }
    public void setIndexId(int indexId) { this.indexId = indexId; }
    public boolean isOutlineNode() { return outlineNode; }
    public void setOutlineNode(boolean outlineNode) { this.outlineNode = outlineNode; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
