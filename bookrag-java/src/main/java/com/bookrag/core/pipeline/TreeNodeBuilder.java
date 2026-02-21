package com.bookrag.core.pipeline;

import com.bookrag.core.index.NodeType;
import com.bookrag.core.index.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNodeBuilder {

    private static final Logger log = LoggerFactory.getLogger(TreeNodeBuilder.class);

    public static TreeNode createNodeByType(Map<String, Object> pdfContent, boolean isTitle) {
        String contentType = (String) pdfContent.getOrDefault("type", "unknown");

        switch (contentType) {
            case "text":
                return createTextNode(pdfContent, isTitle);
            case "image":
                return createImageNode(pdfContent);
            case "table":
                return createTableNode(pdfContent);
            case "equation":
                return createEquationNode(pdfContent);
            default:
                log.warn("Unknown content type: {}. Defaulting to text.", contentType);
                return createDefaultTextNode(pdfContent);
        }
    }

    private static TreeNode createTextNode(Map<String, Object> pdfContent, boolean isTitle) {
        Map<String, Object> nodeMeta = new HashMap<>();
        nodeMeta.put("content", pdfContent.getOrDefault("text", ""));
        nodeMeta.put("pdf_id", pdfContent.getOrDefault("pdf_id", -1));
        nodeMeta.put("page_idx", pdfContent.getOrDefault("page_idx", -1));
        nodeMeta.put("pdf_para_block", pdfContent.getOrDefault("middle_json", new HashMap<>()));

        if (isTitle) {
            Object levelObj = pdfContent.getOrDefault("text_level", -1);
            int level;
            if (levelObj instanceof String) {
                try {
                    level = Integer.parseInt((String) levelObj);
                } catch (NumberFormatException e) {
                    level = -1;
                    isTitle = false;
                }
            } else {
                level = ((Number) levelObj).intValue();
            }
            if (isTitle) {
                nodeMeta.put("title_level", level);
            }
        }

        TreeNode node = new TreeNode(nodeMeta);
        node.setType(isTitle ? NodeType.TITLE : NodeType.TEXT);
        node.setOutlineNode(isTitle);
        return node;
    }

    private static TreeNode createImageNode(Map<String, Object> pdfContent) {
        String captionStr = joinListToString(pdfContent.get("image_caption"));
        String footnoteStr = joinListToString(pdfContent.get("image_footnote"));

        Map<String, Object> nodeMeta = new HashMap<>();
        nodeMeta.put("img_path", pdfContent.getOrDefault("img_path", ""));
        nodeMeta.put("caption", captionStr);
        nodeMeta.put("footnote", footnoteStr);
        nodeMeta.put("content", captionStr + footnoteStr);
        nodeMeta.put("pdf_id", pdfContent.getOrDefault("pdf_id", -1));
        nodeMeta.put("page_idx", pdfContent.getOrDefault("page_idx", -1));
        nodeMeta.put("pdf_para_block", pdfContent.getOrDefault("middle_json", new HashMap<>()));

        TreeNode node = new TreeNode(nodeMeta);
        node.setType(NodeType.IMAGE);
        return node;
    }

    private static TreeNode createTableNode(Map<String, Object> pdfContent) {
        String captionStr = joinListToString(pdfContent.get("table_caption"));
        String footnoteStr = joinListToString(pdfContent.get("table_footnote"));

        Map<String, Object> nodeMeta = new HashMap<>();
        nodeMeta.put("img_path", pdfContent.getOrDefault("img_path", ""));
        nodeMeta.put("caption", captionStr);
        nodeMeta.put("footnote", footnoteStr);
        nodeMeta.put("content", captionStr + footnoteStr);
        nodeMeta.put("table_body", pdfContent.getOrDefault("table_body", ""));
        nodeMeta.put("pdf_id", pdfContent.getOrDefault("pdf_id", -1));
        nodeMeta.put("page_idx", pdfContent.getOrDefault("page_idx", -1));
        nodeMeta.put("pdf_para_block", pdfContent.getOrDefault("middle_json", new HashMap<>()));

        TreeNode node = new TreeNode(nodeMeta);
        node.setType(NodeType.TABLE);
        return node;
    }

    private static TreeNode createEquationNode(Map<String, Object> pdfContent) {
        Map<String, Object> nodeMeta = new HashMap<>();
        nodeMeta.put("content", pdfContent.getOrDefault("text", ""));
        nodeMeta.put("pdf_id", pdfContent.getOrDefault("pdf_id", -1));
        nodeMeta.put("page_idx", pdfContent.getOrDefault("page_idx", -1));
        nodeMeta.put("pdf_para_block", pdfContent.getOrDefault("middle_json", new HashMap<>()));
        nodeMeta.put("text_format", pdfContent.getOrDefault("text_format", ""));

        TreeNode node = new TreeNode(nodeMeta);
        node.setType(NodeType.EQUATION);
        return node;
    }

    private static TreeNode createDefaultTextNode(Map<String, Object> pdfContent) {
        Map<String, Object> nodeMeta = new HashMap<>();
        nodeMeta.put("content", pdfContent.getOrDefault("text", ""));
        nodeMeta.put("pdf_id", pdfContent.getOrDefault("pdf_id", -1));
        nodeMeta.put("page_idx", pdfContent.getOrDefault("page_idx", -1));
        nodeMeta.put("pdf_para_block", pdfContent.getOrDefault("middle_json", new HashMap<>()));

        TreeNode node = new TreeNode(nodeMeta);
        node.setType(NodeType.TEXT);
        return node;
    }

    @SuppressWarnings("unchecked")
    private static String joinListToString(Object obj) {
        if (obj instanceof List) {
            return String.join(" ", (List<String>) obj);
        }
        return "";
    }
}
