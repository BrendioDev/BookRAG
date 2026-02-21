package com.bookrag.core.index;

import java.util.HashMap;
import java.util.Map;

public class MetaInfo {

    // document info
    private String fileName;
    private String filePath;

    // page info
    private Integer pageIdx;
    private String pagePath;

    // item info from PDF extractor
    private Integer pdfId;
    private Map<String, Object> pdfParaBlock;

    // image and table info
    private String imgPath;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private String caption;
    private String footnote;

    // table info
    private String tableBody;

    // text info
    private String content;

    // title info
    private int titleLevel = -1;

    // equation info
    private String textFormat;

    public MetaInfo() {}

    public MetaInfo(Map<String, Object> dict) {
        if (dict == null) return;
        this.fileName = (String) dict.get("file_name");
        this.filePath = (String) dict.get("file_path");
        this.pageIdx = dict.containsKey("page_idx") ? ((Number) dict.get("page_idx")).intValue() : null;
        this.pagePath = (String) dict.get("page_path");
        this.pdfId = dict.containsKey("pdf_id") ? ((Number) dict.get("pdf_id")).intValue() : null;
        this.imgPath = (String) dict.get("img_path");
        if (dict.containsKey("image_width")) this.imageWidth = ((Number) dict.get("image_width")).intValue();
        if (dict.containsKey("image_height")) this.imageHeight = ((Number) dict.get("image_height")).intValue();
        this.caption = (String) dict.get("caption");
        this.footnote = (String) dict.get("footnote");
        this.tableBody = (String) dict.get("table_body");
        this.content = (String) dict.get("content");
        if (dict.containsKey("title_level")) this.titleLevel = ((Number) dict.get("title_level")).intValue();
        this.textFormat = (String) dict.get("text_format");
    }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Integer getPageIdx() { return pageIdx; }
    public void setPageIdx(Integer pageIdx) { this.pageIdx = pageIdx; }
    public String getPagePath() { return pagePath; }
    public void setPagePath(String pagePath) { this.pagePath = pagePath; }
    public Integer getPdfId() { return pdfId; }
    public void setPdfId(Integer pdfId) { this.pdfId = pdfId; }
    public Map<String, Object> getPdfParaBlock() { return pdfParaBlock; }
    public void setPdfParaBlock(Map<String, Object> pdfParaBlock) { this.pdfParaBlock = pdfParaBlock; }
    public String getImgPath() { return imgPath; }
    public void setImgPath(String imgPath) { this.imgPath = imgPath; }
    public int getImageWidth() { return imageWidth; }
    public void setImageWidth(int imageWidth) { this.imageWidth = imageWidth; }
    public int getImageHeight() { return imageHeight; }
    public void setImageHeight(int imageHeight) { this.imageHeight = imageHeight; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public String getFootnote() { return footnote; }
    public void setFootnote(String footnote) { this.footnote = footnote; }
    public String getTableBody() { return tableBody; }
    public void setTableBody(String tableBody) { this.tableBody = tableBody; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getTitleLevel() { return titleLevel; }
    public void setTitleLevel(int titleLevel) { this.titleLevel = titleLevel; }
    public String getTextFormat() { return textFormat; }
    public void setTextFormat(String textFormat) { this.textFormat = textFormat; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (fileName != null) map.put("file_name", fileName);
        if (filePath != null) map.put("file_path", filePath);
        if (pageIdx != null) map.put("page_idx", pageIdx);
        if (pagePath != null) map.put("page_path", pagePath);
        if (pdfId != null) map.put("pdf_id", pdfId);
        if (pdfParaBlock != null) map.put("pdf_para_block", pdfParaBlock);
        if (imgPath != null) map.put("img_path", imgPath);
        if (imageWidth != 0) map.put("image_width", imageWidth);
        if (imageHeight != 0) map.put("image_height", imageHeight);
        if (caption != null) map.put("caption", caption);
        if (footnote != null) map.put("footnote", footnote);
        if (tableBody != null) map.put("table_body", tableBody);
        if (content != null) map.put("content", content);
        if (titleLevel != -1) map.put("title_level", titleLevel);
        if (textFormat != null) map.put("text_format", textFormat);
        return map;
    }
}
