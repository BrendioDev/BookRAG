package com.bookrag.core.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentTree {

    private static final Logger log = LoggerFactory.getLogger(DocumentTree.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private List<TreeNode> nodes = new ArrayList<>();
    private MetaInfo metaInfo;
    private TreeNode rootNode;
    private String saveDir;
    private Map<Integer, Integer> pdfIdToIndexId = new HashMap<>();
    private int maxDepth = -1;

    public DocumentTree(Map<String, Object> metaDict, String savePath) {
        this.metaInfo = new MetaInfo(metaDict);
        this.saveDir = savePath;
        if (metaDict != null) {
            initRootNode(metaDict);
        }
    }

    public void initRootNode(Map<String, Object> metaDict) {
        this.rootNode = new TreeNode(metaDict);
        this.rootNode.setIndexId(0);
        this.rootNode.setDepth(0);
        this.rootNode.setType(NodeType.ROOT);
        if (this.rootNode.getMetaInfo() != null) {
            this.rootNode.getMetaInfo().setPdfId(0);
        }
        this.nodes.add(this.rootNode);
    }

    public List<TreeNode> getNodes(boolean hasRoot) {
        if (hasRoot) return nodes;
        return nodes.size() > 1 ? nodes.subList(1, nodes.size()) : Collections.emptyList();
    }

    public String getOutline() {
        if (rootNode == null) return "";
        StringBuilder sb = new StringBuilder();
        for (TreeNode child : rootNode.getChildren()) {
            for (int[] entry : child.getOutlineEntries()) {
                String title = nodes.get(entry[1]).getMetaInfo().getContent();
                sb.append(entry[0]).append("\t").append(title).append("\t").append(entry[1]).append("\n");
            }
        }
        return sb.toString().stripTrailing();
    }

    public void addNode(TreeNode node) {
        node.setIndexId(nodes.size());
        nodes.add(node);
        Integer pdfId = node.getMetaInfo().getPdfId();
        if (pdfId != null) {
            pdfIdToIndexId.put(pdfId, node.getIndexId());
        }
    }

    public TreeNode getNodeByIndexId(int nodeId) {
        if (nodeId >= 0 && nodeId < nodes.size()) {
            return nodes.get(nodeId);
        }
        return null;
    }

    public List<TreeNode> getNodesByIds(List<Integer> idList) {
        return idList.stream()
                .filter(i -> i >= 0 && i < nodes.size())
                .map(nodes::get)
                .collect(Collectors.toList());
    }

    public TreeNode getNodeByPdfId(int pdfId) {
        Integer nodeIdx = pdfIdToIndexId.get(pdfId);
        if (nodeIdx != null) return nodes.get(nodeIdx);
        if (nodes.size() > pdfId && nodes.get(pdfId).getMetaInfo().getPdfId() != null
                && nodes.get(pdfId).getMetaInfo().getPdfId() == pdfId) {
            return nodes.get(pdfId);
        }
        for (TreeNode node : nodes) {
            if (node.getMetaInfo().getPdfId() != null && node.getMetaInfo().getPdfId() == pdfId) {
                return node;
            }
        }
        return null;
    }

    public int getMaxDepth() {
        if (maxDepth != -1) return maxDepth;
        if (rootNode == null) return 0;
        maxDepth = 0;
        for (TreeNode node : nodes) {
            if (node.getDepth() > maxDepth) maxDepth = node.getDepth();
        }
        return maxDepth;
    }

    public List<TreeNode> getPathFromRoot(int nodeId) {
        TreeNode node = getNodeByIndexId(nodeId);
        if (node == null) return Collections.emptyList();

        List<TreeNode> path = new ArrayList<>();
        Set<Integer> visitedIds = new HashSet<>();

        while (node != null) {
            if (node.getParent() == null) break;
            if (node.getIndexId() == 0) break;
            if (visitedIds.contains(node.getIndexId())) break;
            path.add(node);
            visitedIds.add(node.getIndexId());
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    public List<TreeNode> getSiblingNodes(int nodeId) {
        TreeNode node = getNodeByIndexId(nodeId);
        if (node == null || node.getParent() == null) return Collections.emptyList();
        return node.getParent().getChildren().stream()
                .filter(s -> s.getIndexId() != nodeId)
                .collect(Collectors.toList());
    }

    public List<TreeNode> getSubtreeNodes(List<Integer> nodeIds) {
        Map<Integer, TreeNode> uniqueNodes = new LinkedHashMap<>();
        Set<Integer> visited = new HashSet<>();
        for (int nodeId : nodeIds) {
            getSubtreeRecursive(nodeId, uniqueNodes, visited);
        }
        return new ArrayList<>(uniqueNodes.values());
    }

    private void getSubtreeRecursive(int nodeId, Map<Integer, TreeNode> uniqueNodes, Set<Integer> visited) {
        if (visited.contains(nodeId)) return;
        TreeNode node = getNodeByIndexId(nodeId);
        if (node == null) return;
        visited.add(nodeId);
        uniqueNodes.put(nodeId, node);
        for (TreeNode child : node.getChildren()) {
            getSubtreeRecursive(child.getIndexId(), uniqueNodes, visited);
        }
    }

    public TreeNode getAncestorAtDepth(int nodeId, int depth) {
        TreeNode node = getNodeByIndexId(nodeId);
        if (node == null || depth < 0) return null;
        while (node != null && node.getDepth() > depth) {
            node = node.getParent();
        }
        return (node != null && node.getDepth() == depth) ? node : null;
    }

    public List<TreeNode> getNodesAtDepth(int depth) {
        if (depth < 0) return Collections.emptyList();
        return nodes.stream().filter(n -> n.getDepth() == depth).collect(Collectors.toList());
    }

    public List<TreeNode> getFilteredNodes(NodeType nodeType) {
        return nodes.stream().filter(n -> n.getType() == nodeType).collect(Collectors.toList());
    }

    public Map<String, Object> toJsonSummary() {
        List<Map<String, Object>> nodeSummaries = new ArrayList<>();
        for (TreeNode node : nodes) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("index_id", node.getIndexId());
            entry.put("parent_id", node.getParent() != null ? node.getParent().getIndexId() : null);
            entry.put("type", node.getType() != null ? node.getType().toString() : null);
            entry.put("meta_info", node.getMetaInfo() != null ? node.getMetaInfo().toMap() : null);
            entry.put("summary", node.getSummary());
            nodeSummaries.add(entry);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nodes", nodeSummaries);
        result.put("meta_info", metaInfo != null ? metaInfo.toMap() : null);
        return result;
    }

    public void saveToFile() {
        File dir = new File(saveDir);
        if (!dir.exists()) dir.mkdirs();

        File jsonFile = new File(saveDir, "tree.json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, toJsonSummary());
            log.info("Document tree saved to {}", jsonFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to save document tree to {}", jsonFile.getAbsolutePath(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static DocumentTree loadFromFile(String saveDir) {
        File jsonFile = new File(saveDir, "tree.json");
        try {
            Map<String, Object> data = mapper.readValue(jsonFile, Map.class);

            Map<String, Object> metaMap = (Map<String, Object>) data.get("meta_info");
            DocumentTree tree = new DocumentTree(metaMap, saveDir);

            List<Map<String, Object>> nodeList = (List<Map<String, Object>>) data.get("nodes");
            if (nodeList == null || nodeList.isEmpty()) return tree;

            // Skip index 0 (root node already created in constructor)
            for (int i = 1; i < nodeList.size(); i++) {
                Map<String, Object> nodeData = nodeList.get(i);
                Map<String, Object> metaInfo = (Map<String, Object>) nodeData.get("meta_info");
                TreeNode node = new TreeNode(metaInfo);

                String typeStr = (String) nodeData.get("type");
                node.setType(NodeType.fromString(typeStr));
                node.setSummary((String) nodeData.getOrDefault("summary", ""));

                tree.addNode(node);

                // Wire parent-child
                Object parentIdObj = nodeData.get("parent_id");
                if (parentIdObj != null) {
                    int parentId = ((Number) parentIdObj).intValue();
                    TreeNode parentNode = tree.getNodeByIndexId(parentId);
                    if (parentNode != null) {
                        parentNode.addChild(node);
                    }
                }
            }

            log.info("Document tree loaded from {}, {} nodes", jsonFile.getAbsolutePath(), tree.getNodeCount());
            return tree;
        } catch (IOException e) {
            log.error("Failed to load document tree from {}", jsonFile.getAbsolutePath(), e);
            return new DocumentTree(null, saveDir);
        }
    }

    public static String getSavePath(String inputDir) {
        return new File(inputDir, "tree.json").getPath();
    }

    public TreeNode getRootNode() { return rootNode; }
    public MetaInfo getMetaInfo() { return metaInfo; }
    public String getSaveDir() { return saveDir; }
    public void setSaveDir(String saveDir) { this.saveDir = saveDir; }
    public int getNodeCount() { return nodes.size(); }
}
