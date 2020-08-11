package com.zukron.note.model;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/7/2020
 */
public class DefaultNote {
    private Integer id;
    private String title;
    private String body;

    public DefaultNote(Integer id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

}
