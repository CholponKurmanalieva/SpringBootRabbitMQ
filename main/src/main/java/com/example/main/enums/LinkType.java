package com.example.main.enums;

public enum LinkType {
    DOC_DOWNLOAD("/file/doc"),
    PHOTO_DOWNLOAD("/file/photo");

    private final String link;

    LinkType(String link) {
       this.link =link;
    }

    @Override
    public String toString() {
        return link;
    }
}