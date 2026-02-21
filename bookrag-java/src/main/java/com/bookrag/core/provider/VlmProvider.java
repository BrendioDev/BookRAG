package com.bookrag.core.provider;

import java.util.List;
import java.util.Map;

public interface VlmProvider {

    String generate(String prompt, List<String> imagePaths);

    Map<String, Object> generateJson(String prompt, List<String> imagePaths);
}
