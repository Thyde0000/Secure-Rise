package com.example.securerise.service.impl;

import com.example.securerise.audioplayerservice.AudioService;
import com.example.securerise.dto.AlarmDTO;
import com.example.securerise.repository.AlarmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.stream.Stream;

@SpringBootTest
class AlarmServiceImplementationTest {

    @InjectMocks
    private AlarmServiceImplementation alarmService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AudioService audioService;
    @Mock
    private AlarmRepository alarmRepository;

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


    @ParameterizedTest
    @MethodSource("individualMockDTOs")
    void getAlarm(AlarmDTO alarmDTO) {

    }

    @Test
    void getAlarms() {

    }

    @Test
    void setAlarm() {

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