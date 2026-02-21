package com.bookrag.core.provider.mock;

import com.bookrag.core.provider.VlmProvider;

import java.util.List;
import java.util.Map;

public class MockVlmProvider implements VlmProvider {

    @Override
    public String generate(String prompt, List<String> imagePaths) {
        System.out.println("[MockVLM] generate called, images: " + (imagePaths != null ? imagePaths.size() : 0));
        return "Mock VLM response";
    }

    @Override
    public Map<String, Object> generateJson(String prompt, List<String> imagePaths) {
        System.out.println("[MockVLM] generateJson called");
        return Map.of("result", "mock_vlm");
    }
}
