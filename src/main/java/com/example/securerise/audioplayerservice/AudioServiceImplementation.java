package com.example.securerise.audioplayerservice;

import org.springframework.stereotype.Service;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


@Service
public class AudioServiceImplementation implements AudioService{

    private Clip currentClip;
    public void playAlarmSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(path);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        currentClip = AudioSystem.getClip();
        currentClip.open(audioStream);
        currentClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void stopAlarmSound() {
        if(currentClip != null){
            currentClip.stop();
            currentClip.close();
        }
    }
}
