package io.github.GoosePatrol;

public enum SpriteDirection {
    FORWARD("forward"),
    FORWARD_INCLINE("forward-incline"),
    SIDE("side"),
    BACKWARD_INCLINE("backward-incline"),
    BACKWARD("backward");

    public final String label;

    private SpriteDirection(String label) {
        this.label = label;
    }
}
