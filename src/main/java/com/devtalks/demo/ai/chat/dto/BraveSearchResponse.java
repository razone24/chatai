package com.devtalks.demo.ai.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BraveSearchResponse {
    
    @JsonProperty("web")
    private WebResults web;
    
    public WebResults getWeb() {
        return web;
    }
    
    public void setWeb(WebResults web) {
        this.web = web;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebResults {
        @JsonProperty("results")
        private List<WebResult> results;
        
        public List<WebResult> getResults() {
            return results;
        }
        
        public void setResults(List<WebResult> results) {
            this.results = results;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebResult {
        @JsonProperty("title")
        private String title;
        
        @JsonProperty("url")
        private String url;
        
        @JsonProperty("description")
        private String description;
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
    }
} 