package com.bookrag.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MineruConfig {

    private String backend;
    private String method;
    private String lang;

    @JsonProperty("server_url")
    private String serverUrl = "http://127.0.0.1:30000";

    public MineruConfig() {}

    public String getBackend() { return backend; }
    public void setBackend(String backend) { this.backend = backend; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }
    public String getServerUrl() { return serverUrl; }
    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }
}
