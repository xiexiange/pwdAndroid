package com.autox.module.view.recyclerviews.entity;

public class ListEntity {
    String name;
    int drawableId;
    public ListEntity(String name, int drawableId) {
        this.name = name;
        this. drawableId = drawableId;
    }

    public String name() {
        return name;
    }

    public int drawableId() {
        return drawableId;
    }
}
