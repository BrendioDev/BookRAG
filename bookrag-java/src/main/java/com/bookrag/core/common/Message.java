package com.bookrag.core.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Message {

    public static final String MESSAGE_ROUTE_TO_ALL = "<all>";

    private String id;
    private String content;
    private String role;
    private String sentFrom;
    private Set<String> sendTo;

    public Message(String content) {
        this(content, "user", "", Set.of(MESSAGE_ROUTE_TO_ALL));
    }

    public Message(String content, String role, String sentFrom, Set<String> sendTo) {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.content = content;
        this.role = role;
        this.sentFrom = sentFrom != null ? sentFrom : "";
        this.sendTo = sendTo != null ? sendTo : new HashSet<>(Set.of(MESSAGE_ROUTE_TO_ALL));
    }

    public Map<String, String> toDict() {
        return Map.of("role", role, "content", content);
    }

    @Override
    public String toString() {
        return role + ": " + content;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getSentFrom() { return sentFrom; }
    public void setSentFrom(String sentFrom) { this.sentFrom = sentFrom; }
    public Set<String> getSendTo() { return sendTo; }
    public void setSendTo(Set<String> sendTo) { this.sendTo = sendTo; }
}
