package com.example.securerise.audioplayerservice;

import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
@Service
public interface AudioService {
    void playAlarmSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException;

    void stopAlarmSound();
}
