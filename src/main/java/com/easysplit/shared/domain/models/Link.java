package com.easysplit.shared.domain.models;

/**
 * Object to be serialized and used to store links to resources
 */
public class Link {
    private String href;
    private String method;
    private String type;

    public Link() {}

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
