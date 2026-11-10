package io.github.GoosePatrol;

public enum GooseAction {
    PATROL("walk"),
    CHASE("walk"),
    ATTRACRED("walk"),
    FLEE("walk");

    public final String label;

    private GooseAction(String label) {
        this.label = label;
    }
}

