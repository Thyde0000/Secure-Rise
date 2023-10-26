package com.example.securerise.service.impl;

import com.example.securerise.audioplayerservice.AudioService;
import com.example.securerise.dto.AlarmDTO;
import com.example.securerise.entity.Alarm;
import com.example.securerise.repository.AlarmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AlarmServiceTest {

    @InjectMocks
    private AlarmServiceImplementation alarmService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AlarmRepository alarmRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAlarm() {
        LocalTime time = LocalTime.now();
        Alarm alarm = new Alarm(1L, "Alarm", time, "sound1.wav", false, false);
        AlarmDTO expectedAlarm = new AlarmDTO(1L, "Alarm", time, "sound1.wav", false, false);

        Mockito.when(alarmRepository.findById(1L)).thenReturn(Optional.of(alarm));
        Mockito.when(modelMapper.map(alarm, AlarmDTO.class)).thenReturn(expectedAlarm);

        AlarmDTO result = alarmService.getAlarm(1L);
        assertEquals(expectedAlarm, result);
    }

    @Test
    void getAlarms() {

    }

    @Test
    void setAlarm() {
        LocalTime time = LocalTime.now();
        AlarmDTO alarm = new AlarmDTO(1L, "Alarm", time, "sound1.wav", false, false);
        AlarmDTO expectedAlarm = new AlarmDTO(1L, "Alarm", time, "sound1.wav", false, true);
        AlarmDTO result = alarmService.setAlarm(alarm);
        assertEquals(expectedAlarm,result);

    }

    @Test
    void deleteAlarm() {

    }

    @Test
    void editAlarm() {

    }

    @Test
    void turnOnAlarm() {

    }

    @Test
    void turnOffAlarm() {

    }

    @Test
    void playAlarm() {

    }

    @Test
    void stopAlarm() {

    }
}