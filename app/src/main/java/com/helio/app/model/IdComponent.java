package com.helio.app.model;

public abstract class IdComponent {
    private final int id;

    protected IdComponent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
