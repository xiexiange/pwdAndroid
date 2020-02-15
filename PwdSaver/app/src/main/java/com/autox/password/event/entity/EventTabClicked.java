package com.autox.password.event.entity;

public class EventTabClicked {
    private String text;
    public EventTabClicked(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
