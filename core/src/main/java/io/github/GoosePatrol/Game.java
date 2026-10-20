package io.github.GoosePatrol;

/**
 * The Game class used for managing the overall game state.
 * 
 */
public class Game{
    private boolean paused;
    
    public Game(){
        paused = true;
        this.loadGame();
    }

    /**
     * Loads the initial game window on the main menu.
     */
    public void loadGame(){
        Main main = new Main();
        main.create();
    }
    
    /**
     * Updates the game state when game components change.
     * @param deltaTime the time since the last frame.
     * @return true if the game logic was updated, else returns false.
     */

    public boolean update(float deltaTime){
        if(!paused){
            // update game logic here
            // ie update player position
            return true;
        }
        return false;
    }
    
    public boolean isPaused(){
        return paused;
    }

    public void pauseGame(){
        paused = true;
    }

    public void unpauseGame(){
        paused = false;
    }
}
