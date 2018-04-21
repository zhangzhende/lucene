package com.lucene.filesearch.model;

/**
 * Created by zhangzhende on 2018/3/3.
 */
public class FileModel {

    private String title;
    private String content;

    public FileModel() {
    }

    public FileModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
