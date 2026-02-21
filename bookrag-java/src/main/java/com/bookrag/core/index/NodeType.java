package com.bookrag.core.index;

public enum NodeType {
    ROOT("root"),
    TEXT("text"),
    IMAGE("image"),
    TABLE("table"),
    EQUATION("equation"),
    TITLE("title"),
    UNKNOWN("unknown");

    private final String value;

    NodeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NodeType fromString(String s) {
        if (s == null) return UNKNOWN;
        for (NodeType t : values()) {
            if (t.value.equalsIgnoreCase(s)) return t;
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return value;
    }
}
