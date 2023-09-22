package com.example.securerise.controller;

import com.example.securerise.dto.AlarmDTO;
import com.example.securerise.service.AlarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AlarmControllerTest {
    @InjectMocks
    private AlarmController alarmController;
    @Mock
    private AlarmService alarmService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    private static Stream<Arguments> individualMockDTOs() {
        LocalTime localTime = LocalTime.now();

        return Stream.of(
                Arguments.of(new AlarmDTO(1L, "Alarm 1", localTime, "sound1.wav", false, false)),
                Arguments.of(new AlarmDTO(99L, "Alarm 2", localTime, "sound3.wav", true, false)),
                Arguments.of(new AlarmDTO(12L, "Alarm 3", localTime, "sound.wav", false, true))
        );
    }

    private static Stream<Arguments> pairedMockDTOs() {
        LocalTime localTime = LocalTime.now();
        return Stream.of(
                Arguments.of(
                        new AlarmDTO(1L, "Alarm 1", localTime, "sound1.wav", false, false),
                        new AlarmDTO(1L, "Updated Alarm 1", localTime, "updated_sound1.wav", true, true)
                ),
                Arguments.of(
                        new AlarmDTO(99L, "Alarm 2", localTime, "sound3.wav", true, false),
                        new AlarmDTO(99L, "Updated Alarm 2", localTime, "updated_sound3.wav", false, true)
                ),
                Arguments.of(
                        new AlarmDTO(12L, "Alarm 3", localTime, "sound.wav", false, true),
                        new AlarmDTO(12L, "Updated Alarm 3", localTime, "updated_sound.wav", true, false)
                )
        );
    }

    @Test
    void getAlarms() {
        List<AlarmDTO> mockedAlarms = new ArrayList<>();
        LocalTime localTime = LocalTime.now();
        mockedAlarms.add(new AlarmDTO(1L,"Alarm 1", localTime,"sound1.wav",false,false));
        mockedAlarms.add(new AlarmDTO(2L,"Alarm 2", localTime,"sound2.wav",false,true));
        mockedAlarms.add(new AlarmDTO(3L,"Alarm 3", localTime,"sound3.wav",true,false));

        when(alarmService.getAlarms()).thenReturn(mockedAlarms);
        ResponseEntity<List<AlarmDTO>> response = alarmController.getAlarms();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(mockedAlarms,response.getBody());
    }

    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void getAlarm(AlarmDTO alarmDTO) {
        when(alarmService.getAlarm(alarmDTO.getId())).thenReturn(alarmDTO);
        ResponseEntity<AlarmDTO> response = alarmController.getAlarm(alarmDTO.getId());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(alarmDTO,response.getBody());
    }

    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void setAlarm(AlarmDTO alarmDTO) {
        when(alarmService.setAlarm(alarmDTO)).thenReturn(alarmDTO);
        ResponseEntity<AlarmDTO> response = alarmController.setAlarm(alarmDTO);
        assertEquals(response.getStatusCode(),HttpStatus.CREATED);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(),alarmDTO);
    }

    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void deleteAlarm(AlarmDTO alarmDTO) {
        when(alarmService.deleteAlarm(alarmDTO.getId())).thenReturn("Deleted Successfully");
        ResponseEntity<String> response = alarmController.deleteAlarm(alarmDTO.getId());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @ParameterizedTest
    @MethodSource("pairedMockDTOs")
    void editAlarm(AlarmDTO currentAlarm, AlarmDTO updatedAlarm) {
        when(alarmService.editAlarm(currentAlarm.getId(),updatedAlarm)).thenReturn(updatedAlarm);
        ResponseEntity<AlarmDTO> response = alarmController.editAlarm(currentAlarm.getId(), updatedAlarm);
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(),updatedAlarm);
    }

    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void turnOnAlarm(AlarmDTO alarmDTO) {
        when(alarmService.turnOnAlarm(alarmDTO.getId())).thenReturn("Alarm Turned On");
        ResponseEntity<String> response = alarmController.turnOnAlarm(alarmDTO.getId());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void turnOffAlarm(AlarmDTO alarmDTO) {
        when(alarmService.turnOffAlarm(alarmDTO.getId())).thenReturn("Alarm Turned Off");
        ResponseEntity<String> response = alarmController.turnOffAlarm(alarmDTO.getId());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void playAlarm(AlarmDTO alarmDTO) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        when(alarmService.playAlarm(alarmDTO.getId())).thenReturn("Alarm Playing");
        ResponseEntity<String> response = alarmController.playAlarm(alarmDTO.getId());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }

    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void stopAlarm(AlarmDTO alarmDTO) {
        when(alarmService.stopAlarm(alarmDTO.getId())).thenReturn("Stopped Alarm");
        ResponseEntity<String> response = alarmController.stopAlarm(alarmDTO.getId());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
        assertNotNull(response.getBody());
    }
}