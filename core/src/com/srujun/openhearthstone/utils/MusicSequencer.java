package com.srujun.openhearthstone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Original: http://www.java-gaming.org/topics/libgdx-queue-sound-effects/32864/view.html
 */
public class MusicSequencer {
    private Array<Music> musics = new Array<Music>();
    private int index = 0; // index of last added music
    private boolean hasSequenceStarted = false;
    private boolean sequenceComplete = false;

    /** Add a music to the sequencer. */
    public void addMusic(FileHandle file) {
        addMusic(Gdx.audio.newMusic(file));
    }

    /** Add a music to the sequencer. */
    public void addMusic(final Music newMusic) {
        if(index > 0) {
            musics.get(index - 1).setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    newMusic.play();
                }
            });
        }
        musics.add(newMusic);
        index++;
    }

    /**
     * Play the sequence.
     */
    public void play() {
        if(index == 0)
            return;
        hasSequenceStarted = true;
        musics.get(0).play();
        musics.get(musics.size - 1).setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                sequenceComplete = true;
            }
        });
    }

    /**
     * Check if the sequence playback has started.
     * @return Whether sequence playback has started.
     */
    public boolean hasStarted() {
        return hasSequenceStarted;
    }

    /**
     * Check if the sequence playback is complete.
     * @return Whether sequence playback is complete.
     */
    public boolean isComplete() {
        return sequenceComplete;
    }

    /**
     * Dispose all musics.
     */
    public void dispose() {
        for(int i = 0; i < musics.size; i++) {
            musics.get(i).dispose();
        }
    }
}
