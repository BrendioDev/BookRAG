package com.bookrag.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    /**
     * Load a SystemConfig from a YAML file path.
     * Replicates the Python transform: wraps the raw "rag" section inside {"strategy_config": ...}.
     */
    public static SystemConfig loadSystemConfig(String path) {
        try {
            JsonNode root = YAML_MAPPER.readTree(new File(path));
            return parseSystemConfig(root);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load system config from: " + path, e);
        }
    }

    /**
     * Load a SystemConfig from a classpath resource (for tests).
     */
    public static SystemConfig loadSystemConfigFromResource(String resourcePath) {
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            JsonNode root = YAML_MAPPER.readTree(is);
            return parseSystemConfig(root);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load system config from resource: " + resourcePath, e);
        }
    }

    private static SystemConfig parseSystemConfig(JsonNode root) {
        try {
            // Replicate Python's transform: wrap rag section inside strategy_config
            if (root.has("rag") && root.get("rag").isObject()) {
                JsonNode ragData = root.get("rag");
                ObjectNode wrappedRag = YAML_MAPPER.createObjectNode();
                wrappedRag.set("strategy_config", ragData);
                ((ObjectNode) root).set("rag", wrappedRag);
            }
            return YAML_MAPPER.treeToValue(root, SystemConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse system config", e);
        }
    }

    /**
     * Load a DatasetConfig from a YAML file path.
     */
    public static DatasetConfig loadDatasetConfig(String path) {
        try {
            return YAML_MAPPER.readValue(new File(path), DatasetConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dataset config from: " + path, e);
        }
    }

    /**
     * Load a DatasetConfig from a classpath resource (for tests).
     */
    public static DatasetConfig loadDatasetConfigFromResource(String resourcePath) {
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return YAML_MAPPER.readValue(is, DatasetConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dataset config from resource: " + resourcePath, e);
        }
    }
}
