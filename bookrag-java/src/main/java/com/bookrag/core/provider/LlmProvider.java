package com.bookrag.core.provider;

import java.util.List;
import java.util.Map;

public interface LlmProvider {

    String getCompletion(String prompt);

    Map<String, Object> getJsonCompletion(String prompt);

    List<String> batchGetCompletion(List<String> prompts);
}
