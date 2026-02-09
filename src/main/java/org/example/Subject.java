package org.example;

public enum Subject {
    MATH("Mathematics"),
    JAVA("Java"),
    ENGLISH("English");
//    HISTORY("History");

    private final String displayName;

    Subject(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}