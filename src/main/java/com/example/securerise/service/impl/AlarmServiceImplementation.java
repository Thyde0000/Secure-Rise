package com.example.securerise.service.impl;

import com.example.securerise.audioplayerservice.AudioService;
import com.example.securerise.dto.AlarmDTO;
import com.example.securerise.entity.Alarm;
import com.example.securerise.exceptions.ResourceNotFoundException;
import com.example.securerise.repository.AlarmRepository;
import com.example.securerise.service.AlarmService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@AllArgsConstructor
@Service
public class AlarmServiceImplementation implements AlarmService {
    private AlarmRepository alarmRepository;
    private AudioService audioService;
    private ModelMapper modelMapper;
    private Queue<AlarmDTO> queue;

    @Override
    public AlarmDTO getAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Alarm", "id", id));
        return modelMapper.map(alarm, AlarmDTO.class);
    }

    @Override
    public List<AlarmDTO> getAlarms() {
        List<Alarm> alarmList = alarmRepository.findAll();
        // Custom comparator to compare alarms by time
        Comparator<Alarm> alarmComparator = (alarm1, alarm2) -> {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time1 = LocalTime.parse(alarm1.getStartingTime().toString(), timeFormatter);
            LocalTime time2 = LocalTime.parse(alarm2.getStartingTime().toString(), timeFormatter);
            return time1.compareTo(time2);
        };
        alarmList.sort(alarmComparator);
        return alarmList.stream().map(alarm -> modelMapper.map(alarm, AlarmDTO.class)).toList();
    }

    @Override
    public AlarmDTO setAlarm(AlarmDTO alarmDTO) {
        Alarm alarm = modelMapper.map(alarmDTO, Alarm.class);
        alarm.setEnabled(true);
        Alarm savedAlarm = alarmRepository.save(alarm);
        return modelMapper.map(savedAlarm, AlarmDTO.class);
    }

    @Override
    public AlarmDTO peekQueue() {
        return queue.peek();
    }

    @Override
    public String queueAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alarm","id",id));
        AlarmDTO alarmDTO = modelMapper.map(alarm,AlarmDTO.class);
        queue.offer(alarmDTO);
        return "Queued Alarm";
    }

    @Override
    public String deQueueAlarm() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        AlarmDTO alarm = queue.poll();
        assert alarm != null;
        playAlarm(alarm.getId());
        return "Playing Alarm";
    }

    @Override
    public String deleteAlarm(Long id) {
        alarmRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Alarm","id", id));
        alarmRepository.deleteById(id);
        return "Successfully Deleted";
    }

    @Override
    public AlarmDTO editAlarm(Long id, AlarmDTO alarmDTO) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Alarm","id",id));
        alarm.setAlarmName(alarmDTO.getAlarmName());
        alarm.setStartingTime(alarmDTO.getStartingTime());
        alarm.setAlarmSound(alarmDTO.getAlarmSound());
        alarm.setPlayingSound(alarmDTO.isPlayingSound());
        alarm.setEnabled(alarmDTO.isEnabled());
        alarmRepository.save(alarm);
        return modelMapper.map(alarm,AlarmDTO.class);
    }

    @Override
    public String turnOnAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Alarm","id", id));
        alarm.setEnabled(true);
        alarmRepository.save(alarm);
        return "Alarm Turned On";
    }

    @Override
    public String turnOffAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Alarm","id", id));
        alarm.setEnabled(false);
        alarm.setPlayingSound(false);
        alarmRepository.save(alarm);
        return "Alarm Turned Off";
    }

    @Override
    public String playAlarm(Long id) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Alarm alarm = alarmRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Alarm","id", id));
        alarm.setPlayingSound(true);
        alarmRepository.save(alarm);
        audioService.playAlarmSound(alarm.getAlarmSound());
        return "Alarm Playing";
    }

    @Override
    public String stopAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Alarm", "id", id));
        alarm.setPlayingSound(false);
        alarm.setEnabled(false);
        alarmRepository.save(alarm);
        audioService.stopAlarmSound();
        return "Stopped Alarm";
    }
}