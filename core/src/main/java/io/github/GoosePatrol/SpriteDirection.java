package io.github.GoosePatrol;

/**
 *
 */
public enum SpriteDirection {
    /**
     * Corresponds to a front animation.
     */
    FORWARD("forward"),
    /**
     * Corresponds to a front incline animation.
     */
    FORWARD_INCLINE("forward-incline"),
    /**
     * Corresponds to a side animation.
     */
    SIDE("side"),
    /**
     * Corresponds to a backward incline animation.
     */
    BACKWARD_INCLINE("backward-incline"),
    /**
     * Corresponds to a backward animation.
     */
    BACKWARD("backward");

    /**
     * Allows the code to pass through the label of the enum to be parsed into an animation
     * corresponding to where the player is pointing.
     */
    public final String label;

    private SpriteDirection(String label) {
        this.label = label;
    }
}
