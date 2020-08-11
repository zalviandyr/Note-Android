package com.zukron.note.model;

import java.util.ArrayList;

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 8/8/2020
 */
public class ListNote {
    private Integer id;
    private String title;
    private ArrayList<Boolean> checks;
    private ArrayList<String> items;

    public ListNote(Integer id, String title, ArrayList<Boolean> checks, ArrayList<String> items) {
        this.id = id;
        this.title = title;
        this.checks = checks;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Boolean> getChecks() {
        return checks;
    }

    public ArrayList<String> getItems() {
        return items;
    }
}
