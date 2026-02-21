package com.bookrag.core.pipeline;

import com.bookrag.core.config.SystemConfig;
import com.bookrag.core.index.DocumentTree;
import com.bookrag.core.index.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocTreeBuilderStage implements PipelineStage {

    private static final Logger log = LoggerFactory.getLogger(DocTreeBuilderStage.class);

    @Override
    public String name() { return "DocTreeBuilder"; }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(SystemConfig config, Map<String, Object> context) {
        String savePath = config.getSavePath();

        // Check for existing tree
        String treePath = DocumentTree.getSavePath(savePath);
        if (new File(treePath).exists()) {
            log.info("Existing tree found at {}, loading...", treePath);
            DocumentTree tree = DocumentTree.loadFromFile(savePath);
            context.put("document_tree", tree);
            return;
        }

        // Create new tree
        Map<String, Object> metaDict = new HashMap<>();
        metaDict.put("file_name", new File(config.getPdfPath()).getName());
        metaDict.put("file_path", config.getPdfPath());

        new File(savePath).mkdirs();
        DocumentTree tree = new DocumentTree(metaDict, savePath);

        // Get data from context (populated by earlier stages)
        List<Map<String, Object>> pdfList = (List<Map<String, Object>>) context.get("pdf_list");
        List<Map<String, Object>> titleOutline = (List<Map<String, Object>>) context.get("title_outline");

        if (pdfList == null || titleOutline == null) {
            log.warn("pdf_list or title_outline not found in context. Saving empty tree.");
            tree.saveToFile();
            context.put("document_tree", tree);
            return;
        }

        constructTreeIndex(tree, pdfList, titleOutline);

        tree.saveToFile();
        context.put("document_tree", tree);
        log.info("Document tree built with {} nodes", tree.getNodeCount());
    }

    public void constructTreeIndex(DocumentTree tree, List<Map<String, Object>> pdfList,
                                   List<Map<String, Object>> titleOutline) {
        for (Map<String, Object> content : titleOutline) {
            TreeNode node = TreeNodeBuilder.createNodeByType(content, true);
            tree.addNode(node);

            // Wire parent-child by text_level / parent_id
            int textLevel = ((Number) content.getOrDefault("text_level", -1)).intValue();
            if (textLevel == 0) {
                tree.getRootNode().addChild(node);
            } else {
                Object parentIdObj = content.get("parent_id");
                if (parentIdObj != null) {
                    int parentId = ((Number) parentIdObj).intValue();
                    TreeNode parentNode = tree.getNodeByPdfId(parentId);
                    if (parentNode != null) {
                        parentNode.addChild(node);
                    } else {
                        tree.getRootNode().addChild(node);
                    }
                } else {
                    tree.getRootNode().addChild(node);
                }
            }

            // Add content child nodes in range [pdf_id, end_id)
            int pdfId = ((Number) content.getOrDefault("pdf_id", -1)).intValue();
            int endIdx = ((Number) content.getOrDefault("end_id", -1)).intValue();

            for (int i = pdfId; i < endIdx; i++) {
                if (i >= pdfList.size()) break;

                Map<String, Object> childContent = pdfList.get(i);
                int contentId = ((Number) childContent.getOrDefault("pdf_id", -1)).intValue();
                if (contentId > pdfId && contentId < endIdx) {
                    TreeNode childNode = TreeNodeBuilder.createNodeByType(childContent, false);
                    tree.addNode(childNode);
                    node.addChild(childNode);
                }
            }
        }

        log.info("Total {} nodes added to the tree index.", tree.getNodeCount());
    }
}
