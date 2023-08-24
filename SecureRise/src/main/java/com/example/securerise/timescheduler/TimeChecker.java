package com.example.securerise.timescheduler;

import com.example.securerise.dto.AlarmDTO;
import com.example.securerise.service.impl.AlarmServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class TimeChecker{
    @Autowired
    private AlarmServiceImplementation alarmService;
    Queue<AlarmDTO> alarmQueue = new LinkedList<>();
    private boolean currentAlarmActive = false;

    @Scheduled(initialDelay = 0, fixedRate = 59000)
    public void checkTime() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.now();
        String formattedCurrentHour = String.format("%02d", localTime.getHour());
        String formattedCurrentMinute = String.format("%02d", localTime.getMinute());
        String formattedCurrentTime = formattedCurrentHour + ":" + formattedCurrentMinute;
        LocalTime currentTime = LocalTime.parse(formattedCurrentTime,formatter);
        List<AlarmDTO> alarms = alarmService.getAlarms();
        for(AlarmDTO alarm : alarms){
            LocalTime alarmStartTime = alarm.getStartingTime();
            if(currentTime.equals(alarmStartTime) && alarmQueue.isEmpty() && !currentAlarmActive){
                alarmService.playAlarm(alarm.getId());
                currentAlarmActive = true;
                break;
            }
            else if(currentTime.equals(alarmStartTime) && currentAlarmActive){
                if(!alarmQueue.contains(alarm)){
                    alarmQueue.offer(alarm);
                    System.out.println("Queued Alarm");
                }
            }
            //To Be Implemented Whenever an Alarm Stops, Check if Queue Empty, if Not Play Next Alarm
            else if(!currentTime.equals(alarmStartTime) && !alarmQueue.isEmpty() && !currentAlarmActive){
                AlarmDTO nextAlarmToPlay = alarmQueue.poll();
                alarmService.playAlarm(nextAlarmToPlay.getId());
                currentAlarmActive = true;
                break;
            }
        }
    }
}
