package io.github.GoosePatrol;

/**
 *
 */
public enum PlayerAction {
    /**
     * Corresponds to an idle animation.
     */
    IDLE("idle"),
    /**
     * Corresponds to a walk animation.
     */
    WALK("walk"),
    /**
     * Corresponds to a feed animation.
     */
    FEED("feed"),
    /**
     * Corresponds to an interact animation.
     */
    INTERACT("interact"),
    /**
     * Corresponds to the sleep animation.
     */
    SLEEP("sleeping");

    /**
     * Allows the code to pass through the label of the enum to be parsed into an animation
     * corresponding to what action the player is doing.
     */
    public final String label;

    private PlayerAction(String label) {
        this.label = label;
    }
}
