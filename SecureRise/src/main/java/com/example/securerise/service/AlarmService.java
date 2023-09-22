package com.example.securerise.service;

import com.example.securerise.dto.AlarmDTO;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

@Service
public interface AlarmService {
    AlarmDTO getAlarm(Long id);

    List<AlarmDTO> getAlarms();
    AlarmDTO setAlarm(AlarmDTO alarmDTO);

    AlarmDTO peekQueue();

    String queueAlarm(Long id);

    String deQueueAlarm() throws UnsupportedAudioFileException, LineUnavailableException, IOException;

    String deleteAlarm(Long id);

    AlarmDTO editAlarm(Long id, AlarmDTO alarmDTO);

    String turnOnAlarm(Long id);

    String turnOffAlarm(Long id);

    String playAlarm(Long id) throws UnsupportedAudioFileException, LineUnavailableException, IOException;

    String stopAlarm(Long id);
}
