package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Audio {
    private float masterVolume = 1f;
    private float musicVolume = 1f;
    private float sfxVolume = 1f;

    private Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Vivaldi_La-Follia.mp3"));

    public Audio(){
        music.setLooping(true);
    }

    public void setMasterVolume(float volume){
        masterVolume = volume;
        music.setVolume(volume * masterVolume);
    }

    public void setMusicVolume(float volume){
        musicVolume = volume;
        music.setVolume(volume * masterVolume);
    }

    public void setSfxVolume(float volume){
        sfxVolume = volume;
    }

    public void playMusic(){
        music.play();
    }

    public void stopMusic(){
        music.stop();
    }
}
