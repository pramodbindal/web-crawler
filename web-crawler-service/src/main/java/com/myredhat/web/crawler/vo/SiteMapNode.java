package com.myredhat.web.crawler.vo;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SiteMapNode {


    final private String url;
    private Set<SiteMapNode> childNodes;

    final int nodeLevel;

    @JsonIgnore
    private final boolean external;

    public SiteMapNode(String url, SiteMapNode patentNode, boolean external) {
        this.url = url;
        this.nodeLevel = patentNode.getNodeLevel() + 1;
        this.external=external;
    }

    public SiteMapNode(String url) {
        this.url = url;
        this.nodeLevel = 0;
        this.external=false;

    }

    public Set<SiteMapNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(Set<SiteMapNode> childNodes) {
        this.childNodes = childNodes;
    }

    public String getUrl() {
        return url;
    }


    public int getNodeLevel() {
        return nodeLevel;
    }

    public boolean isExternal() {
        return external;
    }

    @Override
    public String toString() {
        return "SiteMapNode{" +
                "url='" + url + '\'' +
                ", childNodes=" + childNodes +
                '}';
    }
}
