package com.bookrag.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PdfContentUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Map<String, Object>> enumeratePdfList(List<Map<String, Object>> pdfList) {
        List<Map<String, Object>> enumerated = new ArrayList<>();
        int i = 1; // pdf_id starts from 1
        for (Map<String, Object> content : pdfList) {
            if (content != null && !Boolean.TRUE.equals(content.get("invalid"))) {
                content.put("pdf_id", i);
                enumerated.add(content);
                i++;
            }
        }
        return enumerated;
    }

    public static String getJsonContent(List<Map<String, Object>> anyList, List<String> selectedColumns) {
        List<Map<String, Object>> jsonList = new ArrayList<>();
        for (Object item : anyList) {
            if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> contentMap = (Map<String, Object>) item;
                Map<String, Object> filtered = new LinkedHashMap<>();
                for (String col : selectedColumns) {
                    if (contentMap.containsKey(col)) {
                        filtered.put(col, contentMap.get(col));
                    }
                }
                jsonList.add(filtered);
            }
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonList);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}
