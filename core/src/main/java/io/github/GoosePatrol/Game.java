package io.github.GoosePatrol;

public class Game{
    private boolean paused;
    
    public Game(){
        paused = true;
        System.out.println("Loading game...");

        this.loadGame();
    }
    /**
     * Loads the initial screen for the game.
     */
    public void loadGame(){
        Main main = new Main();
        
        main.create();
    }
    
    /**
     * Updates the game state.
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
