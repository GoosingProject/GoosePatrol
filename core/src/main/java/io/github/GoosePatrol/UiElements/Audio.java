package io.github.GoosePatrol.UiElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class to handle the volume of each audio channel and to play said audio
 */
public class Audio {
    private float masterVolume = 1f;
    private float musicVolume = 1f;
    private float sfxVolume = 1f;

    Map<String, Music> music = new HashMap<String, Music>();
    Map<String, Sound> sfx = new HashMap<String, Sound>();
    private String currentTrack;

    /**
     * Constructs the audio and sound hashmaps populating them and setting all the music to loop
     */
    public Audio() {
        this.music.put("menu", Gdx.audio.newMusic(Gdx.files.internal("sounds/La-Follia.mp3")));
        this.music.put("ambient", Gdx.audio.newMusic(Gdx.files.internal("sounds/Rondo_Alla_Turka.ogg")));
        this.music.put("fight", Gdx.audio.newMusic(Gdx.files.internal("sounds/La-Follia.mp3")));

        for (Music track : music.values()) {
            track.setLooping(true);
        }

        this.sfx.put("beep", Gdx.audio.newSound(Gdx.files.internal("sounds/beep.ogg")));
    }

    /**
     * Sets the master volume to the specified parameter and updates all tracks volumes
     *
     * @param volume The desired master volume setpoint
     */
    public void setMasterVolume(float volume) {
        masterVolume = volume;

        this.updateTrackVolume();
    }

    /**
     * Sets the music volume to the specified parameter and updates all tracks volumes
     *
     * @param volume The desired music volume setpoint
     */
    public void setMusicVolume(float volume) {
        musicVolume = volume;

        this.updateTrackVolume();
    }

    /**
     * Helper method to set the individual track volumes of the music scaled by master and music volume
     */
    private void updateTrackVolume() {
        for (Music track : music.values()) {
            track.setVolume(musicVolume * masterVolume);
        }
    }

    /**
     * Sets the SFX volume to the specified parameter and plays a beep for feedback
     *
     * @param volume The desired SFX volume setpoint
     */
    public void setSfxVolume(float volume) {
        sfxVolume = volume;

        playSound("beep");
    }

    /**
     * Pauses the current track playing and plays the specified track
     *
     * @param trackKey The track key to be played
     * @throws NullPointerException If the track is not found
     */
    public void playMusic(String trackKey) {
        if (Objects.equals(trackKey, currentTrack)) {
            return;
        }

        this.pauseMusic();

        Music track = music.get(trackKey);
        if (track == null) {
            throw new NullPointerException("Track " + trackKey + " not found");
        }

        track.play();
        currentTrack = trackKey;
    }

    /**
     * Plays a sound scaled by master and SFX volume
     *
     * @param soundKey The sound key to be played
     */
    public void playSound(String soundKey) {
        Sound effect = sfx.get(soundKey);
        if (effect == null) {
            throw new NullPointerException("Sound " + soundKey + " not found");
        }

        effect.play(sfxVolume * masterVolume);
    }

    /**
     * Pauses the current track playing music
     */
    public void pauseMusic() {
        if (currentTrack != null) {
            music.get(currentTrack).pause();
        }
    }

    /**
     * Disposes of all the music tracks and sounds
     */
    public void dispose() {
        for (Music track : music.values()) {
            track.dispose();
        }

        for (Sound effect : sfx.values()) {
            effect.dispose();
        }
    }
}
