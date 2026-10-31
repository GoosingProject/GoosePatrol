package io.github.GoosePatrol;

public enum PlayerAction {
    IDLE("idle"),
    WALK("walk"),
    FEED("feed"),
    INTERACT("interact");

    public final String label;

    private PlayerAction(String label) {
        this.label = label;
    }
}
