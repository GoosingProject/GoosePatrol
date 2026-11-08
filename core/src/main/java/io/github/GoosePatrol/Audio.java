package io.github.GoosePatrol;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class Audio {
    private float masterVolume = 1f;
    private float musicVolume = 1f;
    private float sfxVolume = 1f;

    Map<String, Music> music = new HashMap<String, Music>();
    Map<String, Sound> sfx = new HashMap<String, Sound>();
    private String currentTrack;

    public Audio() {
        music.put("menu", Gdx.audio.newMusic(Gdx.files.internal("sounds/Vivaldi_La-Follia.mp3")));
        music.put("ambient", Gdx.audio.newMusic(Gdx.files.internal("sounds/Mozart_Rondo-Alla-Turca.mp3")));
        music.put("fight", Gdx.audio.newMusic(Gdx.files.internal("sounds/Vivaldi_La-Follia.mp3")));

        for (Music track : music.values()) {
            track.setLooping(true);
        }

        sfx.put("beep", Gdx.audio.newSound(Gdx.files.internal("sounds/beep.ogg")));
    }

    public void setMasterVolume(float volume) {
        masterVolume = volume;

        for (Music track : music.values()) {
            track.setVolume(volume * masterVolume);
        }
    }

    public void setMusicVolume(float volume) {
        musicVolume = volume;

        for (Music track : music.values()) {
            track.setVolume(volume * masterVolume);
        }
    }

    public void setSfxVolume(float volume) {
        sfxVolume = volume;

        playSound("beep");
    }

    public void playMusic(String trackKey) {
        if (Objects.equals(trackKey, currentTrack)) {
            return;
        }

        for (Music track : music.values()) {
            track.pause();
        }

        Music track = music.get(trackKey);
        if (track == null) {
            throw new NullPointerException("Track " + trackKey + " not found");
        }

        track.play();
        currentTrack = trackKey;
    }

    public void playSound(String soundKey) {
        Sound effect = sfx.get(soundKey);
        if (effect == null) {
            throw new NullPointerException("Sound " + soundKey + " not found");
        }

        effect.play(sfxVolume * masterVolume);
    }

    public void stopMusic() {
        for (Music track : music.values()) {
            track.stop();
        }
    }

    public void dispose() {
        for (Music track : music.values()) {
            track.dispose();
        }

        for (Sound effect : sfx.values()) {
            effect.dispose();
        }
    }
}
