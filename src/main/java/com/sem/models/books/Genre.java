package com.sem.models.books;

public enum Genre {
    Detective("Детектив"),
    action("Экшен");
    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String displayName(){
        return displayName;
    }
}
