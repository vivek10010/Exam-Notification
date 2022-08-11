package com.example.examfeverr;

import java.io.Serializable;

public class HorizontalRecViewModel implements Serializable {
    String name, id;

    public HorizontalRecViewModel() {
    }

    public HorizontalRecViewModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
