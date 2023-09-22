package com.example.securerise.controller;

import com.example.securerise.dto.AlarmDTO;

import com.example.securerise.service.AlarmService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/alarm")
public class AlarmController {
    private AlarmService alarmService;

    @GetMapping()
    public ResponseEntity<List<AlarmDTO>> getAlarms(){
        List<AlarmDTO> alarmDTOList = alarmService.getAlarms();
        return new ResponseEntity<>(alarmDTOList,HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<AlarmDTO> getAlarm(@PathVariable Long id){
        AlarmDTO alarmDTO = alarmService.getAlarm(id);
        return new ResponseEntity<>(alarmDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AlarmDTO> setAlarm(@RequestBody @Valid AlarmDTO alarmDTO) {
        AlarmDTO savedAlarm = alarmService.setAlarm(alarmDTO);
        return new ResponseEntity<>(savedAlarm, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAlarm(@PathVariable Long id){
        String deletedResponse = alarmService.deleteAlarm(id);
        return new ResponseEntity<>(deletedResponse, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<AlarmDTO> editAlarm(@PathVariable Long id, @RequestBody @Valid AlarmDTO alarmDTO){
        AlarmDTO savedAlarm = alarmService.editAlarm(id, alarmDTO);
        return new ResponseEntity<>(savedAlarm, HttpStatus.OK);
    }

    @PatchMapping("turnOn/{id}")
    public ResponseEntity<String> turnOnAlarm(@PathVariable Long id){
        String response = alarmService.turnOnAlarm(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("turnOff/{id}")
    public ResponseEntity<String> turnOffAlarm(@PathVariable Long id) {
        String response = alarmService.turnOffAlarm(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping("play/{id}")
    public ResponseEntity<String> playAlarm(@PathVariable Long id) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String response = alarmService.playAlarm(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("stop/{id}")
    public ResponseEntity<String> stopAlarm(@PathVariable Long id){
        String response = alarmService.stopAlarm(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("queue/{id}")
    public ResponseEntity<String> queueAlarm(@PathVariable Long id){
        String response = alarmService.queueAlarm(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("dequeue/")
    public ResponseEntity<String> dequeueAlarm() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String response = alarmService.deQueueAlarm();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("queue")
    public ResponseEntity<AlarmDTO> peekQueue(){
        AlarmDTO nextAlarm = alarmService.peekQueue();
        return new ResponseEntity<>(nextAlarm, HttpStatus.OK);
    }
}
