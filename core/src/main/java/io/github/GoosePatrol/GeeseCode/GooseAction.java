package io.github.GoosePatrol.GeeseCode;

public enum GooseAction {
    PATROL("walk"),
    CHASE("walk"),
    ATTRACT("walk"),
    SCARE("walk");

    public final String label;

    private GooseAction(String label) {
        this.label = label;
    }
}

